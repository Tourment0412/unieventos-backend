package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.exceptions.*;
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
    String createAccount(CreateAccountDTO account) throws DuplicateResourceException, Exception;

    /**
     * Updates an account
     * @param account Data that will be used to update the account
     * @return The id of the updated account
     * @throws Exception
     */
    String updateAccount(UpdateAccountDTO account) throws ResourceNotFoundException;

    /**
     * Deletes an account
     * @param id Id of the account that will be deleted
     * @return The id of the deleted account
     * @throws Exception
     */
    String deleteAccount(String id) throws ResourceNotFoundException;

    /**
     * Gets the information of an account
     * @param id id of the account that it wants to get the information
     * @return The information of an account
     * @throws Exception
     */
    AccountInfoDTO getInfoAccount(String id) throws ResourceNotFoundException;

    /**
     * Sends the recover password code
     * @param email Email of the account that the password will recover
     * @return id of the account
     * @throws Exception
     */
    String sendRecoverPasswordCode(String email) throws ResourceNotFoundException, Exception;

    /**
     * Changes the password of an account
     * @param changePasswordDTO Required data to change the password
     * @return id of the account
     * @throws Exception
     */
    String changePassword(ChangePasswordDTO changePasswordDTO) throws ResourceNotFoundException, ValidationCodeException;

    /**
     * Creates a token to login
     * @param loginDTO Email and password
     * @return The token to login
     * @throws Exception
     */
    TokenDTO login(LoginDTO loginDTO)throws ResourceNotFoundException, AccountNotActivatedException, InvalidPasswordException;

    /**
     * Validates the registration code
     * @param activateAccountDTO Email and registration code
     * @return The id of the account
     * @throws Exception
     */
    String validateRegistrationCode(ActivateAccountDTO activateAccountDTO) throws ResourceNotFoundException, ValidationCodeException, Exception;

    /**
     * Reassigns Validation registration code of an account
     * @param email Email of the account
     * @return The id of the account
     * @throws Exception
     */
    String reassignValidationRegistrationCode(String email) throws ResourceNotFoundException, Exception;

    /**
     * Gets an account with the specified email
     * @param email Email of the account
     * @return The account with the corresponding email
     * @throws Exception
     */
    Account getAccountEmail(String email) throws ResourceNotFoundException;

    /**
     * Gets an account with the specified id
     * @param id id of the account
     * @return The account with the corresponding id
     * @throws Exception
     */
    Account getAccount(String id) throws ResourceNotFoundException;
}
