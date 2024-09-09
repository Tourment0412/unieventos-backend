package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;

public interface AccountService {

    //Tne exception should be a custom exception
    String createAccount(CreateAccountDTO account) throws Exception;

    String updateAccount(UpdateAccountDTO account) throws Exception;

    String deleteAccount(String id) throws Exception;

    AccountInfoDTO getInfoAccount(String id) throws Exception;

    String sendRecoverPasswordCode(String email) throws Exception;

    String changePassword(ChangePasswordDTO changePasswordDTO) throws Exception;

    String login(LoginDTO loginDTO)throws Exception;


}
