package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.User;
import co.edu.uniquindio.unieventos.model.vo.ValidationCode;
import co.edu.uniquindio.unieventos.repositories.AccountRepo;
import co.edu.uniquindio.unieventos.services.interfaces.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional()
public class AccountServiceImp implements AccountService {
    //This repo is final because we are not going to modify the repo
    private final AccountRepo accountRepo;

    public AccountServiceImp(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public String createAccount(CreateAccountDTO account) throws Exception {
        if (existsDni(account.dni())){
            //Remember adding custom exceptions
            throw new Exception("Account with this dni already exists");
        }

        if(existsEmail(account.email())){
            throw new Exception("Account with this email already exists");
        }

        Account newAccount = new Account();
        //Te id on the account class is given by mongodb
        accountRepo.save(newAccount);
        newAccount.setEmail(account.email());
        newAccount.setPassword(account.password());
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
        newAccount.setRegistrationValidationCode(new ValidationCode(LocalDateTime.now(), generateValidationCode()));
        
        //TODO Method for mail sending for activation on account

        Account accountCreated= accountRepo.save(newAccount);
        return accountCreated.getId();
    }

    private boolean existsEmail(String email) {
        return accountRepo.findAccountByEmail(email).isPresent();
    }

    private boolean existsDni(String dni){
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
        return "Account deleted successfully";
    }

    private Account getAccount(String id) throws Exception {
        Optional<Account> accountOptional= accountRepo.findAccountById(id);
        if(accountOptional.isEmpty()){
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
        Account account = getAccount1(email);
        String validationCode = generateValidationCode();
        //TODO Send this code to the user (Account) email

        account.setPasswordValidationCode(new ValidationCode(LocalDateTime.now(),validationCode));
        accountRepo.save(account);
        return "A validation code has been sent to your email, check your email, it lasts 15 minutes.";
    }

    private Account getAccount1(String email) throws Exception {
        Optional<Account> accountOptional= accountRepo.findAccountByEmail(email);
        if(accountOptional.isEmpty()){
            throw new Exception("This email is not registered");
        }
        return accountOptional.get();
    }

    @Override
    public String changePassword(ChangePasswordDTO changePasswordDTO) throws Exception {
        Optional<Account> accountOptional= accountRepo.findAccountByEmail(changePasswordDTO.email());
        if(accountOptional.isEmpty()){
            throw new Exception("This email is not registered");
        }
        Account account = accountOptional.get();
        ValidationCode passwordValidationCode = account.getPasswordValidationCode();
        if(passwordValidationCode !=null){
            if(passwordValidationCode.getCode().equals(changePasswordDTO.verificationCode())){
                if(passwordValidationCode.getCreationDate().plusMinutes(15).isBefore(LocalDateTime.now())){
                    account.setPassword(changePasswordDTO.newPassword());
                    accountRepo.save(account);
                }else{
                    account.setPasswordValidationCode(null);
                    accountRepo.save(account);
                    throw new Exception("This verification code has expired");
                }
            }else{
                throw new Exception("This verification is incorrect");
            }
        }
        return "The password has been changed successfully";
    }

    @Override
    public String login(LoginDTO loginDTO) throws Exception {
        Optional<Account> accouOptional = accountRepo.validateAuthenticationData(loginDTO.email(),loginDTO.password());
        if(accouOptional.isEmpty()){
            throw new Exception("Authentication data incorrect");
        }
        //TODO This is return is going to be a token that's going to be sent to the backend
        return "TOKEN_JWT";
    }
}
