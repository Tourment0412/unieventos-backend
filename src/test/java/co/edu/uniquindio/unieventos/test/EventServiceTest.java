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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepo eventRepo;

    String eventId="6706047ac127c9d5e7e16cc8";

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
        event.setId("6707574f8330fb37e7f74969");
        event.setName("Event to Delete");
        event.setStatus(EventStatus.ACTIVE);
        eventRepo.save(event);

        String result = eventService.deleteEvent(event.getId());

        assertEquals("6707574f8330fb37e7f74969", result);
        Event deletedEvent = eventRepo.findById(event.getId()).orElse(null);
        assertNotNull(deletedEvent);
        assertEquals(EventStatus.INACTIVE, deletedEvent.getStatus());
    }

    @Test
    void testGetInfoEvent() throws Exception {

        EventInfoDTO eventInfo = eventService.getInfoEventClient("6706047ac127c9d5e7e16cc6");

        //TODO Modificar valores compraracion (Preguntar si comparaciones son suficioente)
        assertNotNull(eventInfo);
        assertEquals("Concierto de Rock", eventInfo.name());
        assertEquals("Calle 10 #25-35", eventInfo.address());
        assertEquals("2024-11-15T15:00", eventInfo.date().toString());
        assertEquals("Concierto de rock con las mejores bandas locales " +
                "e internacionales.", eventInfo.description());
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
        /*
        List<EventItemDTO> events = eventService.listEventsAdmin(0);

        assertTrue(events.size()>0 && events.size()<=10);
        eventRepo.delete(event1);
        eventRepo.delete(event2);

         */

    }

    @Test
    void testListEventsClient() {
        Event event = new Event();
        event.setName("Event for Client");
        event.setStatus(EventStatus.ACTIVE);
        eventRepo.save(event);
        /*
        List<EventItemDTO> events = eventService.listEventsClient(0);

        assertTrue(events.size()>0 && events.size()<=10);
        eventRepo.delete(event);
         */
    }

    @Test
    void testFilterEventsClient() {

        EventFilterDTO filterDTO = new EventFilterDTO("Evento de Belleza", EventType.BEAUTY, "Cartagena", 0);

        List<EventItemDTO> filteredEvents = eventService.filterEventsClient(filterDTO);

        assertEquals(1, filteredEvents.size());
        assertEquals("Evento de Belleza", filteredEvents.get(0).name());

    }

    @Test
    void testFilterEventsAdmin() {

        EventFilterDTO filterDTO = new EventFilterDTO("Concierto de Rock", EventType.CONCERT, "Bogotá", 0);

        List<EventItemDTO> filteredEvents = eventService.filterEventsAdmin(filterDTO);

        assertEquals(1, filteredEvents.size());
        assertEquals("Concierto de Rock", filteredEvents.get(0).name());
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

    //////////////////////////////////////////////
    @Test
    void testGetInfoEventAdmin() throws Exception {
        EventInfoAdminDTO eventInfoAdminDTO=null;
        try {
            eventInfoAdminDTO=eventService.getInfoEventAdmin("6706047ac127c9d5e7e16cc6");
            assertEquals("Concierto de Rock", eventInfoAdminDTO.name());
            assertEquals("Calle 10 #25-35",eventInfoAdminDTO.address());
            assertEquals("cover_rock_concert.jpg",eventInfoAdminDTO.coverImage());
            assertEquals("localities_rock_concert.jpg",eventInfoAdminDTO.localitiesImage());
            assertEquals("2024-11-15T15:00",eventInfoAdminDTO.date().toString());
            assertEquals("Concierto de rock con las mejores bandas locales e " +
                    "internacionales.",eventInfoAdminDTO.description());
            assertEquals("CONCERT", eventInfoAdminDTO.type().toString());
            assertEquals("ACTIVE", eventInfoAdminDTO.status().toString());
            assertEquals(2,eventInfoAdminDTO.locations().size());
        } catch (Exception e) {
            assertTrue(false);
        }
        assertNotNull(eventInfoAdminDTO);

    }


}
