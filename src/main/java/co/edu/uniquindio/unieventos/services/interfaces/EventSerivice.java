package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;

import java.util.List;

public interface EventSerivice {

    String createEvent(CreateEventDTO createEventDTO);
    String updateEvent(UpdateEventDTO updateEventDTO);
    String deleteEvent(String id);
    EventInfoDTO getInfoEvent();
    List<EventItemDTO> listEvents();
    //The filter depends on the requirements of the  project
    List<EventItemDTO> filterEvents(EventFilterDTO eventFilterDTO);
}
