package co.edu.uniquindio.unieventos.repositories;

import co.edu.uniquindio.unieventos.dto.eventdtos.LocationPercentageDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.LocationQuantityDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.LocationSalesDTO;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    /*
    @Query("{ $and: [ " +
            " { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'name': { $exists: true } } ] }, " +
            " { $or: [ { 'type': ?1 }, { 'type': { $exists: true } } ] }, " +  // Manejo de tipo de evento
            " { $or: [ { 'city': { $regex: ?2, $options: 'i' } }, { 'city': { $exists: true } } ] }, " +  // Filtrado por ciudad
            " { 'date': { $gte: new Date()} }, " +  // Fecha igual o posterior a la actual
            " { 'status': 'ACTIVE' } " +  // Estado ACTIVE
            "] }")
    Page<Event> findEventsByFiltersClient(String name, EventType eventType, String city,Pageable pageable);
    */
    @Aggregation({
            "{ $match: { $and: [ ?0, { 'date': { $gte: new Date() } }, { 'status': 'ACTIVE' } ] } }"
    })
    Slice<Event> findEventsByFiltersClient(Map<String, Object> filter,Pageable pageable);

    @Query("{ '_id': ?0, 'status': 'ACTIVE' }")
    Optional<Event> findEventByIdClient(String id);

    @Query("{" +
            "'status': 'ACTIVE'," +
            "'date': { $gte: new Date() }" +
            "}")
    Page<Event> findAllEventsClient(Pageable pageable);

    /*
    @Query("{ $and: [ " +
            " { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'name': { $exists: true } } ] }, " +
            " { $or: [ { 'type': ?1 }, { 'type': { $exists: true } } ] }, " +  // Manejo de tipo de evento
            " { $or: [ { 'city': { $regex: ?2, $options: 'i' } }, { 'city': { $exists: true } } ] } " +
            "] }")
    Page<Event> findEventsByFiltersAdmin(String name, EventType eventType, String city, Pageable pageable);
    */
    /*
    @Query("{ $or: [ " +
            " { $and: [ { 'name': { $regex: ?0, $options: 'i' } }, { ?0: { $ne: \"\" } } ] }, " +
            " { $and: [ { 'type': ?1 }, { ?1: { $ne: \"\" } } ] }, " +  // Manejo de tipo de evento
            " { $and: [ { 'city': { $regex: ?2, $options: 'i' } }, { ?2: { $ne: \"\" } } ] } " +
            "] }")
    Page<Event> findEventsByFiltersAdmin(String name, EventType eventType, String city, Pageable pageable);
    */

    //Aggregation no se puede usar con Pages al parecer se puede usar solo listas o los slides ya me diran que uso
    @Aggregation({"{ $match: ?0 }"})
    Slice<Event> findEventsByFiltersAdmin(Map<String, Object> filter, Pageable pageable);

    @Aggregation(pipeline = {
            "{ '$match': { '_id': ?0 } }",
            "{ '$unwind': '$locations' }",
            "{ '$project': { " +
                    "   '_id': 0, " + // Excluir el campo _id de la ubicación
                    "   'locationName': '$locations.name', " +
                    "   'percentageSold': { '$multiply': [ { '$divide': ['$locations.ticketsSold', '$locations.maxCapacity'] }, 100 ] }, " +
                    "   'eventId': '$_id' " + // Incluir el ID del evento
                    "} }"
    })
    List<LocationPercentageDTO> calculatePercentageSoldByLocation(ObjectId eventId);

    @Aggregation(pipeline = {
            "{ '$match': { '_id': ?0 } }",
            "{ '$unwind': '$locations' }",
            "{ '$project': { " +
                    "   '_id': 0, " + // Excluir el campo _id de la ubicación
                    "   'locationName': '$locations.name', " +
                    "   'totalSold': { '$multiply': ['$locations.ticketsSold', '$locations.price'] }, " + // Mantener la lógica original
                    "   'eventId': '$_id' " + // Incluir el ID del evento
                    "} }"
    })
    List<LocationSalesDTO> calculateSoldByLocation(ObjectId eventId);

    @Aggregation(pipeline = {
            "{ '$match': { '_id': ?0 } }",
            "{ '$unwind': '$locations' }",
            "{ '$project': { " +
                    "   '_id': 0, " + // Excluir el campo _id de la ubicación
                    "   'locationName': '$locations.name', " +
                    "   'quantitySold': '$locations.ticketsSold', " + // Mantener la lógica original
                    "   'eventId': '$_id' " + // Incluir el ID del evento
                    "} }"
    })
    List<LocationQuantityDTO> calculateQuantitySoldByLocation(ObjectId eventId);
}
