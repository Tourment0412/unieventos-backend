package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCarRepo extends MongoRepository<ShoppingCar, String> {

    /**
     * Finds shopping car by its user id
     * @param id User id of shopping car
     * @return Shopping car with the specified user id
     */
    @Query("{'userId': ?0}")
    Optional<ShoppingCar> findByUserId(ObjectId id);

}
