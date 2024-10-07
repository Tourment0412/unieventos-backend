package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.documents.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends MongoRepository<Order, String> {

    @Query("{_id:  ?0}")
    Optional<Account> findOrderById(String id);

    @Query("{date:  ?0}")
    Optional<Account> findOrderByDate(LocalDate date);

    @Query("{ 'clientId': ?0 }")
    List<Order> findOrdersByClientId(ObjectId clientId);

    @Query("{ 'items.eventId': ?0 }")
    List<Order> findByItemsEventId(ObjectId eventId);

    @Query("{ 'items.eventId': ?0 }")
    List<Order> findByEventId(ObjectId eventId);
}
