package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.util.Map;

public interface AccountService {

    //Tne exception should be a custom exception
    /**
     * Creates an account
     * @param account Data that will be used to create the account
     * @return The id of the created account
     * @throws Exception
     */
    String createAccount(CreateAccountDTO account) throws Exception;

    /**
     * Updates an account
     * @param account Data that will be used to update the account
     * @return The id of the updated account
     * @throws Exception
     */
    String updateAccount(UpdateAccountDTO account) throws Exception;

    /**
     * Deletes an account
     * @param id Id of the account that will be deleted
     * @return The id of the deleted account
     * @throws Exception
     */
    String deleteAccount(String id) throws Exception;

    /**
     * Gets the information of an account
     * @param id id of the account that it wants to get the information
     * @return The information of an account
     * @throws Exception
     */
    AccountInfoDTO getInfoAccount(String id) throws Exception;

    /**
     * Sends the recover password code
     * @param email Email of the account that the password will recover
     * @return id of the account
     * @throws Exception
     */
    String sendRecoverPasswordCode(String email) throws Exception;

    /**
     * Changes the password of an account
     * @param changePasswordDTO Required data to change the password
     * @return id of the account
     * @throws Exception
     */
    String changePassword(ChangePasswordDTO changePasswordDTO) throws Exception;

    /**
     * Creates a token to login
     * @param loginDTO Email and password
     * @return The token to login
     * @throws Exception
     */
    TokenDTO login(LoginDTO loginDTO)throws Exception;

    /**
     * Validates the registration code
     * @param activateAccountDTO Email and registration code
     * @return The id of the account
     * @throws Exception
     */
    String validateRegistrationCode(ActivateAccountDTO activateAccountDTO) throws Exception;

    /**
     * Reassigns Validation registration code of an account
     * @param email Email of the account
     * @return The id of the account
     * @throws Exception
     */
    String reassignValidationRegistrationCode(String email) throws Exception;

    /**
     * Gets an account with the specified email
     * @param email Email of the account
     * @return The account with the corresponding email
     * @throws Exception
     */
    Account getAccountEmail(String email) throws Exception;

    /**
     * Gets an account with the specified id
     * @param id id of the account
     * @return The account with the corresponding id
     * @throws Exception
     */
    Account getAccount(String id) throws Exception;
}
