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
    @Query("{'user': { $ne: null }, 'user.dni':  ?0}")
    Optional<Account> findAccountByDni(String dni);

    @Query("{'id':  ?0}")
    Optional<Account> findAccountById(String id);

    //In this way simple queries like this one are going to be done without the query annotation
    //YOU SHOULD TO BE CAREFUL WITH THE SYNTAX OF THE NAME OF THIS METHOD
    Optional<Account> findByUserDni(String dni);

    @Query("{'email':  ?0}")
    Optional<Account> findAccountByEmail(String email);

    @Query("{email: ?0, password: ?1}")
    Optional<Account> validateAuthenticationData(String email, String password);

    @Query(value = "{ 'email': ?0, 'password': ?1 }", fields = "{ 'user.phoneNumber' : 1, 'user.address' : 1, " +
            "'user.dni' :1, 'user.name': 1  }")
    UserObtainedByMailPassDTO findUserObtainedByMailPassDTOByDni(String email, String password);
}
