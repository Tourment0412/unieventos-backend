package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.eventdtos.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventFilterDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventItemDTO;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    public void createEventTestSuccess(){


        Assertions.assertDoesNotThrow( () ->
                {
                    eventService.createEvent(new CreateEventDTO(
                            "Evento 1",
                            "UQ",
                            "Armenia",
                            "link",
                            "link",
                            LocalDateTime.now(),
                            "Desc tiempo",
                            EventType.CONCERT,
                            List.of(
                                    new Location()
                            )
                    ));

                }
        );

    }

    @Test
    public void createEventTestFailed(){

        Assertions.assertThrows(Exception.class, () ->
                {
                    eventService.createEvent(new CreateEventDTO(
                            "Evento 1",
                            "UQ",
                            "Armenia",
                            "link",
                            "link",
                            LocalDateTime.now(),
                            "Desc tiempo",
                            EventType.CONCERT,
                            List.of(
                                    new Location()
                            )
                    ));

                }
        );

    }

    @Test
    public void filterEventsTest(){

        List<EventItemDTO> events = eventService.filterEventsClient(new EventFilterDTO(
                null,
                EventType.CONCERT,
                null
        ));

        Assertions.assertEquals(1, events.size());

        /*
        List<EventItemDTO> lista = eventService.filterEventsClient( new EventFilterDTO(
                null,
                EventType.CONCERT,
                "Armenia"
        ));
        //this is just an example
        assertEquals(1, lista.size() );*/
    }

}
