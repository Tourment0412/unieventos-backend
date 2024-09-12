package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepo extends MongoRepository<Event, String> {

    /*
        Write a query that returns a list of events of a specific type. Make use of a test
        that allows you to test the performance of the query. Use Pageable.

        At the moment only the query is gonna be used, has doubts on the implementation
        or calling the query
     */

    List<Event> findByType(EventType type, Pageable pageable);

    @Query("{name: ?0}")
    Optional<Event> findByName(String name);


}
