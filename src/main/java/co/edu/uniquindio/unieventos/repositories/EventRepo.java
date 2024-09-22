package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepo extends MongoRepository<Event, String> {

    /*
        Write a query that returns a list of events of a specific type. Make use of a test
        that allows you to test the performance of the query. Use Pageable.

        At the moment only the query is going to be used, has doubts on the implementation
        or calling the query
     */

    List<Event> findByType(EventType type, Pageable pageable);

    @Query("{name: ?0}")
    Optional<Event> findByName(String name);


    /*
        TODO Ask to teacher if the treatment of null values to make them empty strings can be done in controllers
        or can be done on services
     */

    @Query("{ $and: [ " +
            " { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'name': { $exists: true } } ] }, " +
            " { $or: [ { 'type': ?1 }, { 'type': { $exists: true } } ] }, " +  // Manejo de tipo de evento
            " { $or: [ { 'city': { $regex: ?2, $options: 'i' } }, { 'city': { $exists: true } } ] }, " +  // Filtrado por ciudad
            " { 'date': { $gte: ?3} }, " +  // Fecha igual o posterior a la actual
            " { 'status': 'ACTIVE' } " +  // Estado ACTIVE
            "] }")
    List<Event> findEventsByFiltersClient(String name, EventType eventType, String city, LocalDateTime now);


    @Query("{" +
            "'status': 'ACTIVE'," +
            "'date': { $gte: ?0 }" +
            "}")
    List<Event> findAllEventsClient(LocalDateTime now);

    @Query("{ $and: [ " +
            " { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'name': { $exists: true } } ] }, " +
            " { $or: [ { 'type': ?1 }, { 'type': { $exists: true } } ] }, " +  // Manejo de tipo de evento
            " { $or: [ { 'city': { $regex: ?2, $options: 'i' } }, { 'city': { $exists: true } } ] } " +
            "] }")
    List<Event> findEventsByFiltersAdmin(String name, EventType eventType, String city);



}
