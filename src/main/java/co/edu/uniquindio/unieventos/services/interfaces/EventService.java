package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.vo.Location;

import java.util.List;

public interface EventService {

    String createEvent(CreateEventDTO createEventDTO) throws Exception;
    String updateEvent(UpdateEventDTO updateEventDTO) throws Exception;
    String deleteEvent(String id) throws Exception;
    EventInfoAdminDTO getInfoEventAdmin(String id) throws Exception;
    EventInfoDTO getInfoEventClient(String id) throws Exception;
    List<EventItemDTO> listEventsAdmin(int page);
    List<EventItemDTO> listEventsClient(int page);
    //The filter depends on the requirements of the  project
    List<EventItemDTO> filterEventsClient(EventFilterDTO eventFilterDTO);
    List<EventItemDTO> filterEventsAdmin(EventFilterDTO eventFilterDTO);
    Event getEvent(String id) throws Exception;
    public void reduceNumberLocations(int numLocations, String nameLocation, String idEvent) throws Exception;
}
