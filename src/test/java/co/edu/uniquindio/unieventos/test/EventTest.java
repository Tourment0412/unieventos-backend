package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.eventdtos.EventFilterClientDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventItemDTO;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventTest {

    @Autowired
    private EventService eventService;

    @Test
    public void filterEventsTest(){
        /*
        List<EventItemDTO> lista = eventService.filterEventsClient( new EventFilterClientDTO(
                null,
                EventType.CONCERT,
                "Armenia"
        ));
        //this is just an example
        assertEquals(1, lista.size() );*/
    }

}
