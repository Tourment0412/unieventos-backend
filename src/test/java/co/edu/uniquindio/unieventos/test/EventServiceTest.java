package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepo eventRepo;

    String eventId="66fb6cd9c087605dfd69829b";

    @Test
    void testCreateEvent() throws Exception {
        CreateEventDTO createEventDTO = new CreateEventDTO(
                "Test Event",
                "Test Address",
                "Test City",
                "cover.jpg",
                "localities.jpg",
                LocalDateTime.now().plusDays(5),
                "Test Description",
                EventType.CONCERT,
                List.of(new Location())
        );

        String eventId = eventService.createEvent(createEventDTO);

        assertNotNull(eventId);
        Event createdEvent = eventRepo.findById(eventId).orElse(null);
        assertNotNull(createdEvent);
        assertEquals("Test Event", createdEvent.getName());
    }

    @Test
    void testUpdateEvent() throws Exception {
        // Primero creamos un evento para actualizarlo luego

        UpdateEventDTO updateEventDTO = new UpdateEventDTO(
                eventId,
                "Updated Event",
                "New Address",
                "New City",
                "newcover.jpg",
                "newlocalities.jpg",
                LocalDateTime.now().plusDays(5),
                "Updated Description",
                EventType.CONCERT, EventStatus.ACTIVE,
                List.of(new Location())
        );

        String updatedEventId = eventService.updateEvent(updateEventDTO);

        assertEquals(eventId, updatedEventId);
        Event updatedEvent = eventRepo.findById(updatedEventId).orElse(null);
        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getName());
    }

    @Test
    void testDeleteEvent() throws Exception {
        Event event = new Event();
        event.setName("Event to Delete");
        event.setStatus(EventStatus.ACTIVE);
        eventRepo.save(event);

        String result = eventService.deleteEvent(event.getId());

        assertEquals("Event deleted successfully", result);
        Event deletedEvent = eventRepo.findById(event.getId()).orElse(null);
        assertNotNull(deletedEvent);
        assertEquals(EventStatus.INACTIVE, deletedEvent.getStatus());
    }

    @Test
    void testGetInfoEvent() throws Exception {

        EventInfoDTO eventInfo = eventService.getInfoEvent(eventId);

        //TODO Modificar valores compraracion (Preguntar si comparaciones son suficioente)
        assertNotNull(eventInfo);
        assertEquals("Updated Event", eventInfo.name());
        assertEquals("New Address", eventInfo.address());
        assertEquals("2024-10-05T22:33:38.085", eventInfo.date().toString());
        assertEquals("Updated Description", eventInfo.description());
        assertEquals("CONCERT", eventInfo.type().toString());
    }

    @Test
    void testListEventsAdmin() {
        Event event1 = new Event();
        event1.setName("Event 1");
        eventRepo.save(event1);

        Event event2 = new Event();
        event2.setName("Event 2");
        eventRepo.save(event2);

        List<EventItemDTO> events = eventService.listEventsAdmin(0);

        assertEquals(3, events.size());
        assertEquals("Event 1", events.get(1).name());
        assertEquals("Event 2", events.get(2).name());
        eventRepo.delete(event1);
        eventRepo.delete(event2);

    }

    @Test
    void testListEventsClient() {
        Event event = new Event();
        event.setName("Event for Client");
        event.setStatus(EventStatus.ACTIVE);
        eventRepo.save(event);

        List<EventItemDTO> events = eventService.listEventsClient(0);

        assertEquals(1, events.size());
        assertEquals("Updated Event", events.get(0).name());
        eventRepo.delete(event);
    }

    @Test
    void testFilterEventsClient() {
        Event event = new Event();
        event.setName("Filtered Event");
        event.setStatus(EventStatus.ACTIVE);
        eventRepo.save(event);

        EventFilterDTO filterDTO = new EventFilterDTO("Filtered Event", EventType.CONCERT, "Test City", 0);

        List<EventItemDTO> filteredEvents = eventService.filterEventsClient(filterDTO);

        assertEquals(1, filteredEvents.size());
        assertEquals("Filtered Event", filteredEvents.get(1).name());
        eventRepo.delete(event);
    }

    @Test
    void testFilterEventsAdmin() {
        Event event = new Event();
        event.setName("Admin Filtered Event");
        eventRepo.save(event);

        EventFilterDTO filterDTO = new EventFilterDTO("Admin Filtered Event", EventType.CONCERT, "Test City", 0);

        List<EventItemDTO> filteredEvents = eventService.filterEventsAdmin(filterDTO);

        assertEquals(1, filteredEvents.size());
        assertEquals("Admin Filtered Event", filteredEvents.get(0).name());
    }

    @Test
    void testReduceNumberLocations() throws Exception {
        Location location = new Location("VIP", 100, 0, 100);
        Event event = new Event();
        event.setName("Event with Location");
        event.setLocations(List.of(location));
        eventRepo.save(event);

        eventService.reduceNumberLocations(10, "VIP", event.getId());

        Event updatedEvent = eventRepo.findById(event.getId()).orElse(null);
        assertNotNull(updatedEvent);
        assertEquals(10, updatedEvent.findLocationByName("VIP").getTicketsSold());
    }


}
