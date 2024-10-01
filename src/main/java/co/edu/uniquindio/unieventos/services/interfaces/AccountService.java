package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;

public interface AccountService {

    //Tne exception should be a custom exception
    String createAccount(CreateAccountDTO account) throws Exception;

    String updateAccount(UpdateAccountDTO account) throws Exception;

    String deleteAccount(String id) throws Exception;

    AccountInfoDTO getInfoAccount(String id) throws Exception;

    String sendRecoverPasswordCode(String email) throws Exception;

    String changePassword(ChangePasswordDTO changePasswordDTO) throws Exception;

    TokenDTO login(LoginDTO loginDTO)throws Exception;

    String validateRegistrationCode(ActivateAccountDTO activateAccountDTO) throws Exception;

    String reassignValidationRegistrationCode(String email) throws Exception;


}
