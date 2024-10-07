package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.emaildtos.EmailDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.User;
import co.edu.uniquindio.unieventos.model.vo.ValidationCode;
import co.edu.uniquindio.unieventos.repositories.AccountRepo;
import co.edu.uniquindio.unieventos.services.interfaces.AccountService;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImp implements AccountService {
    //This repo is final because we are not going to modify the repo
    private final AccountRepo accountRepo;
    private final EmailService emailService;
    private final JWTUtils jwtUtils;

    public AccountServiceImp(AccountRepo accountRepo, EmailService emailService, JWTUtils jwtUtils) {

        this.accountRepo = accountRepo;
        this.emailService = emailService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public String createAccount(CreateAccountDTO account) throws Exception {
        if (existsDni(account.dni())) {
            //Remember adding custom exceptions
            throw new Exception("Account with this dni already exists");
        }

        if (existsEmail(account.email())) {
            throw new Exception("Account with this email already exists");
        }

        Account newAccount = new Account();
        newAccount.setEmail(account.email());

        String encryptedPassword = encryptPassword(account.password());

        newAccount.setPassword(encryptedPassword);
        //The goal of this register is to be for a client (Admins directly registered on db)
        newAccount.setRole(Role.CLIENT);
        newAccount.setRegistrationDate(LocalDateTime.now());
        //Status on the account is going to be inactive because is needed an activation by mail
        newAccount.setStatus(AccountStatus.INACTIVE);

        newAccount.setUser(User.builder()
                .name(account.name())
                .dni(account.dni())
                .phoneNumber(account.phoneNumber())
                .adress(account.phoneNumber())
                .build());
        String validationCode = generateValidationCode();
        newAccount.setRegistrationValidationCode(new ValidationCode(LocalDateTime.now(), validationCode));

        //TODO Method for mail sending for activation on account
        String subject = "Hey! this is your activation code for your Unieventos account";
        String body = "Your activation code is " + validationCode + " you have 15 minutes to do the activation " +
                "of your Unieventos account.";

        emailService.sendEmail(new EmailDTO(subject, body, account.email()));
        Account accountCreated = accountRepo.save(newAccount);
        return accountCreated.getUser().getDni();
    }


    private boolean existsEmail(String email) {
        return accountRepo.findAccountByEmail(email).isPresent();
    }

    private boolean existsDni(String dni) {
        return accountRepo.findAccountByDni(dni).isPresent();
    }

    private String generateValidationCode() {
        String string = "ABCOEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * string.length());
            char character = string.charAt(index);

            result.append(character);
        }
        return result.toString();
    }

    @Override
    public String updateAccount(UpdateAccountDTO account) throws Exception {

        Account accountToUpdate = getAccount(account.id());

        accountToUpdate.getUser().setName(account.name());
        accountToUpdate.getUser().setAddress(account.address());
        accountToUpdate.getUser().setPhoneNumber(account.phoneNumber());
        accountToUpdate.setPassword(account.password());

        accountRepo.save(accountToUpdate);

        return accountToUpdate.getId();
    }

    @Override
    public String deleteAccount(String id) throws Exception {

        Account accountToDelete = getAccount(id);
        accountToDelete.setStatus(AccountStatus.DELETED);
        accountRepo.save(accountToDelete);
        return "Account deleted successfully";
    }

    @Override
    public Account getAccount(String id) throws Exception {
        Optional<Account> accountOptional = accountRepo.findAccountById(id);
        if (accountOptional.isEmpty()) {
            throw new Exception("Account with this id does not exist");
        }

        return accountOptional.get();
    }

    @Override
    public AccountInfoDTO getInfoAccount(String id) throws Exception {
        Account account = getAccount(id);
        return new AccountInfoDTO(
                account.getId(),
                account.getUser().getDni(),
                account.getUser().getName(),
                account.getUser().getPhoneNumber(),
                account.getUser().getAddress(),
                account.getEmail()
        );
    }

    @Override
    public String sendRecoverPasswordCode(String email) throws Exception {
        Account account = getAccountEmail(email);
        String recoverCode = generateValidationCode();
        //TODO Send this code to the user (Account) email
        String subject = "Recover Password Code";
        String body = "Hey! you have requested the recover of your password code for your Unieventos account\nThis " +
                "is your recover password code: " + recoverCode + "\nThis code lasts 15 minutes.";
        account.setPasswordValidationCode(new ValidationCode(LocalDateTime.now(), recoverCode));
        accountRepo.save(account);
        emailService.sendEmail(new EmailDTO(subject, body, account.getEmail()));
        return "A validation code has been sent to your email, check your email, it lasts 15 minutes.";
    }

    @Override
    public Account getAccountEmail(String email) throws Exception {
        Optional<Account> accountOptional = accountRepo.findAccountByEmail(email);
        if (accountOptional.isEmpty()) {
            throw new Exception("This email is not registered");
        }
        return accountOptional.get();
    }

    @Override
    public String changePassword(ChangePasswordDTO changePasswordDTO) throws Exception {
        Optional<Account> accountOptional = accountRepo.findAccountByEmail(changePasswordDTO.email());
        if (accountOptional.isEmpty()) {
            throw new Exception("This email is not registered");
        }
        Account account = accountOptional.get();
        ValidationCode passwordValidationCode = account.getPasswordValidationCode();
        if (passwordValidationCode != null) {
            if (passwordValidationCode.getCode().equals(changePasswordDTO.verificationCode())) {
                if (passwordValidationCode.getCreationDate().plusMinutes(15).isAfter(LocalDateTime.now())) {
                    account.setPassword(encryptPassword(changePasswordDTO.newPassword()));
                    accountRepo.save(account);
                } else {
                    account.setPasswordValidationCode(null);
                    accountRepo.save(account);
                    throw new Exception("This verification code has expired");
                }
            } else {
                throw new Exception("This verification is incorrect");
            }
        }
        return "The password has been changed successfully";
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {
        Account account = getAccountEmail(loginDTO.email());
        if (account.getStatus() == AccountStatus.DELETED) {
            throw new Exception("Account with this email does not exist");
        }
        if (account.getStatus() == AccountStatus.INACTIVE) {
            throw new Exception("Account with this email is not active");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginDTO.password(), account.getPassword())) {
            throw new Exception("Invalid password");
        }
        Map<String, Object> map = buildClaims(account);
        return new TokenDTO(jwtUtils.generarToken(account.getEmail(), map));
    }

    private Map<String, Object> buildClaims(Account account) {
        return Map.of(
                "role", account.getRole(),
                "name", account.getUser().getName(),
                "status", account.getStatus(),
                "id", account.getId()
        );

    }

    @Override
    public String validateRegistrationCode(ActivateAccountDTO activateAccountDTO) throws Exception {
        Account accountObtained = getAccountEmail(activateAccountDTO.email());
        ValidationCode registrationValidationCode = accountObtained.getRegistrationValidationCode();

        if (registrationValidationCode != null) {
            if (registrationValidationCode.getCode().equals(activateAccountDTO.registrationValidationCode())) {
                if (registrationValidationCode.getCreationDate().plusMinutes(15).isAfter(LocalDateTime.now())) {
                    accountObtained.setStatus(AccountStatus.ACTIVE);
                    accountRepo.save(accountObtained);

                } else {
                    throw new Exception("Registration validation code has expired");
                }
            } else {
                throw new Exception("This registration validation code is incorrect");
            }
        }
        return accountObtained.getId();
    }

    @Override
    public String reassignValidationRegistrationCode(String email) throws Exception {
        Account accountObtained = getAccountEmail(email);
        ValidationCode reassignValidationCode = new ValidationCode(LocalDateTime.now(), generateValidationCode());
        accountObtained.setRegistrationValidationCode(reassignValidationCode);
        String subject = "Hey! this is your NEW activation code for your Unieventos account";
        String body = "Your activation code is " + reassignValidationCode.getCode() + " you have 15 minutes to do the activation " +
                "of your Unieventos account.";
        emailService.sendEmail(new EmailDTO(subject, body, accountObtained.getEmail()));
        accountRepo.save(accountObtained);

        return accountObtained.getId();
    }



    private String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
