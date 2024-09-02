package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends MongoRepository<Account, String> {
    //If we don't use any query instruction is going to do a find()
    //Same syntax as the MongoDB Shell (This is the recommended way to query)
    @Query("{user.dni:  ?0}")
    Optional<Account> findAccountByDni(String dni);

    //In this way simple queries like this one are going to be done without the query annotation
    //YOU SHOULD TO BE CAREFUL WITH THE SYNTAX OF THE NAME OF THIS METHOD
    Optional<Account> findByUserDni(String dni);

    @Query("{user.email:  ?0}")
    Optional<Account> findAccountByEmail(String email);
}
