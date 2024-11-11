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

    /**
     * Finds order by its id
     * @param id id of order
     * @return Order with the specified id
     */
    @Query("{_id:  ?0}")
    Optional<Account> findOrderById(String id);

    /**
     * Finds order by its date
     * @param date Date of order
     * @return Order with the specified date
     */
    @Query("{date:  ?0}")
    Optional<Account> findOrderByDate(LocalDate date);

    /**
     * Finds order by its client id
     * @param clientId Client id of order
     * @return Order with the specified client id
     */
    @Query("{ 'clientId': ?0 }")
    List<Order> findOrdersByClientId(ObjectId clientId);

    /**
     * Finds order by its event id
     * @param eventId Event id of order
     * @return Order with the specified event id
     */
    @Query("{ 'items.eventId': ?0 }")
    List<Order> findByItemsEventId(ObjectId eventId);

    /**
     * Finds order by its event id
     * @param eventId Event id of order
     * @return Order with the specified event id
     */
    @Query("{ 'items.eventId': ?0 }")
    List<Order> findByEventId(ObjectId eventId);

    @Query(value = "{ 'clientId': ?0, 'couponId': ?1 }", exists = true)
    boolean existsByClientIdAndCouponId(ObjectId clientId, ObjectId couponId);


}
