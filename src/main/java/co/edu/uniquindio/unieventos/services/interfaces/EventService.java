package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;

import java.util.List;

public interface EventService {

    String createEvent(CreateEventDTO createEventDTO) throws Exception;
    String updateEvent(UpdateEventDTO updateEventDTO) throws Exception;
    String deleteEvent(String id) throws Exception;
    EventInfoDTO getInfoEvent(String id) throws Exception;
    List<EventItemDTO> listEventsAdmin();
    List<EventItemDTO> listEventsClient();
    //The filter depends on the requirements of the  project
    List<EventItemDTO> filterEvents(EventFilterDTO eventFilterDTO);
}
