package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.dto.orderdtos.EventReportDTO;
import co.edu.uniquindio.unieventos.exceptions.DuplicateResourceException;
import co.edu.uniquindio.unieventos.exceptions.InsufficientCapacityException;
import co.edu.uniquindio.unieventos.exceptions.OperationNotAllowedException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.vo.Location;

import java.util.List;

public interface EventService {

    /**
     * Creates an event
     * @param createEventDTO Data of the event that will be created
     * @return The id of event
     * @throws Exception
     */
    String createEvent(CreateEventDTO createEventDTO) throws DuplicateResourceException;

    /**
     * Updates an event
     * @param updateEventDTO Data that will be used to update the event
     * @return The id of the updated event
     * @throws Exception
     */
    String updateEvent(UpdateEventDTO updateEventDTO) throws ResourceNotFoundException;

    /**
     * Deletes an event
     * @param id id of the event that will be deleted
     * @return The id of the deleted event
     * @throws Exception
     */
    String deleteEvent(String id) throws ResourceNotFoundException;

    /**
     * Gets the information of an event
     * @param id id of the event that it wants to get the information
     * @return The information of an event
     * @throws Exception
     */
    EventInfoAdminDTO getInfoEventAdmin(String id) throws ResourceNotFoundException;

    /**
     * Gets the information of an active event
     * @param id id of the active event that it wants to get the information
     * @return The information of an active event
     * @throws Exception
     */
    EventInfoDTO getInfoEventClient(String id) throws ResourceNotFoundException;

    /**
     * Gests all the events specified by the pagination information
     * @param page The number of the page
     * @return The events specified
     */
    List<EventItemDTO> listEventsAdmin(int page);

    /**
     * Gests all the active events specified by the pagination information
     * @param page The number of the page
     * @return The active events specified
     */
    List<EventItemDTO> listEventsClient(int page);

    /**
     *
     * @param eventFilterDTO
     * @return
     */
    //The filter depends on the requirements of the  project
    List<EventItemDTO> filterEventsClient(EventFilterDTO eventFilterDTO);

    /**
     *
     * @param eventFilterDTO
     * @return
     */
    List<EventItemDTO> filterEventsAdmin(EventFilterDTO eventFilterDTO);

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    Event getEvent(String id) throws ResourceNotFoundException;

    /**
     *
     * @param numLocations
     * @param nameLocation
     * @param idEvent
     * @throws Exception
     */
    void reduceNumberLocations(int numLocations, String nameLocation, String idEvent) throws OperationNotAllowedException, ResourceNotFoundException, InsufficientCapacityException;

    /**
     *
     * @param idEvent
     * @return
     */
    EventReportDTO createReport(String idEvent);


}
