package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.dto.accountdtos.UserObtainedByMailPassDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends MongoRepository<Account, String> {
    //If we don't use any query instruction is going to do a find()
    //Same syntax as the MongoDB Shell (This is the recommended way to query)

    /**
     * Finds account by its dni
     * @param dni dni of account
     * @return Account with the specified dni
     */
    @Query("{'user': { $ne: null }, 'user.dni':  ?0}")
    Optional<Account> findAccountByDni(String dni);

    /**
     * Finds account by its id
     * @param id id of account
     * @return Account with the specified id
     */
    @Query("{'_id':  ?0}")
    Optional<Account> findAccountById(String id);

    //In this way simple queries like this one are going to be done without the query annotation
    //YOU SHOULD TO BE CAREFUL WITH THE SYNTAX OF THE NAME OF THIS METHOD
    Optional<Account> findByUserDni(String dni);

    /**
     * Finds account by its email
     * @param email Email of account
     * @return Account with the specified email
     */
    @Query("{'email':  ?0}")
    Optional<Account> findAccountByEmail(String email);

    /**
     * Finds account by its email and password
     * @param email Email of account
     * @param password Password of account
     * @return Account with the specified email and password
     */
    @Query("{'email': ?0, 'password': ?1}")
    Optional<Account> validateAuthenticationData(String email, String password);

    /**
     * Retrieves a DTO containing limited information about a user.
     * @param email The user's email address.
     * @param password The user's password.
     * @return UserObtainedByMailPassDTO with the filtered user details.
     */
    @Query(value = "{ 'email': ?0, 'password': ?1 }", fields = "{ 'user.phoneNumber' : 1, 'user.address' : 1, " +
            "'user.dni' :1, 'user.name': 1  }")
    UserObtainedByMailPassDTO findUserObtainedByMailPassDTOByDni(String email, String password);
}
