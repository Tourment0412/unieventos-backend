package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.*;
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
        Account accountCreated= accountRepo.save(newAccount);
        return accountCreated.getId();
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
        return "";
    }

    @Override
    public String deleteAccount(String id) throws Exception {
        return "";
    }

    @Override
    public AccountInfoDTO getInfoAccount(String id) throws Exception {
        return null;
    }

    @Override
    public String sendRecoverPasswordCode(String email) throws Exception {
        return "";
    }

    @Override
    public String changePassword(ChangePasswordDTO changePasswordDTO) throws Exception {
        return "";
    }

    @Override
    public String login(LoginDTO loginDTO) throws Exception {
        return "";
    }
}
