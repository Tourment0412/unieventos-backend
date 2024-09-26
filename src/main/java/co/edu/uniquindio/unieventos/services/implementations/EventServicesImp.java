package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class EventServicesImp implements EventService {

    private final EventRepo eventRepo;

    public EventServicesImp(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public String createEvent(CreateEventDTO createEventDTO) throws Exception {

        if (existEventActiveName(createEventDTO.name())) {
            throw new Exception("An event with this name is already active");
        }
        Event event = new Event();

        event.setName(createEventDTO.name());
        event.setAddress(createEventDTO.address());
        event.setCity(createEventDTO.city());
        event.setCoverImage(createEventDTO.coverImage());
        event.setLocalitiesImage(createEventDTO.localitiesImage());
        event.setDate(createEventDTO.date());
        event.setDescription(createEventDTO.description());
        event.setType(createEventDTO.type());
        event.setLocations(createEventDTO.locations());
        //Is going to be active when the event is created
        event.setStatus(EventStatus.ACTIVE);
        Event createdEvent = eventRepo.save(event);

        return createdEvent.getId();
    }

    private boolean existEventActiveName(String name) {

        Optional<Event> eventOptional = eventRepo.findByName(name);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            return event.getStatus() == EventStatus.ACTIVE;
        }
        return false;
    }

    @Override
    public String updateEvent(UpdateEventDTO updateEventDTO) throws Exception {
        Event eventToUpdate = getEvent(updateEventDTO.id());
        eventToUpdate.setName(updateEventDTO.name());
        eventToUpdate.setAddress(updateEventDTO.address());
        eventToUpdate.setCity(updateEventDTO.city());
        eventToUpdate.setCoverImage(updateEventDTO.coverImage());
        eventToUpdate.setLocalitiesImage(updateEventDTO.localitiesImage());
        eventToUpdate.setDate(updateEventDTO.date());
        eventToUpdate.setDescription(updateEventDTO.description());
        eventToUpdate.setType(updateEventDTO.type());
        eventToUpdate.setLocations(updateEventDTO.locations());
        eventToUpdate.setStatus(updateEventDTO.status());
        eventRepo.save(eventToUpdate);
        return eventToUpdate.getId();
    }

    @Override
    public Event getEvent(String id) throws Exception {
        Optional<Event> eventToUpdate = eventRepo.findById(id);
        if (eventToUpdate.isEmpty()) {
            throw new Exception("Event with this id does not exist");
        }
        return eventToUpdate.get();
    }

    @Override
    public Location findLocationByEventNameAndLocationName(String eventName, String locationName) throws Exception {
        List<Location> locationsEvent = eventRepo.findLocationByEventNameAndLocationName(eventName, locationName);
        if(locationsEvent.size() != 1) {
            throw new Exception("Invalid number of Locations with this name");
        }
        return locationsEvent.get(0);
    }

    @Override
    public String deleteEvent(String id) throws Exception {
        Event eventToDelete = getEvent(id);
        eventToDelete.setStatus(EventStatus.INACTIVE);
        eventRepo.save(eventToDelete);

        return "Event deleted successfully";
    }

    @Override
    public EventInfoDTO getInfoEvent(String id) throws Exception {
        Event event = getEvent(id);
        return new EventInfoDTO(
                event.getName(),
                event.getAddress(),
                event.getCoverImage(),
                event.getLocalitiesImage(),
                event.getDate(),
                event.getDescription(),
                event.getType(),
                event.getLocations()
        );

    }

    @Override
    public List<EventItemDTO> listEventsAdmin() {
        //For the admin, all events are going to be shown.
        List<Event> events = eventRepo.findAll();

        return events.stream().map(event -> new EventItemDTO(
                event.getName(),
                event.getDate(),        // Asegúrate de que sea LocalDateTime si es necesario
                event.getAddress(),
                event.getCoverImage()
        )).collect(Collectors.toList());
    }

    @Override
    public List<EventItemDTO> listEventsClient() {
        //listar excluyendo los inactivos y los vencidos
        List<Event> events = eventRepo.findAllEventsClient();
        return events.stream()
                .map(event -> new EventItemDTO(
                        event.getName(),
                        event.getDate(),
                        event.getAddress(),
                        event.getCoverImage()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<EventItemDTO> filterEventsClient(EventFilterDTO eventFilterDTO) {
        List<Event> eventsFiltered = eventRepo.findEventsByFiltersClient(
                eventFilterDTO.name() == null ? "" : eventFilterDTO.name(),
                eventFilterDTO.eventType(),
                eventFilterDTO.city() == null ? "" : eventFilterDTO.city()
        );
        return eventsFiltered.stream()
                .map(event -> new EventItemDTO(
                        event.getName(),
                        event.getDate(),
                        event.getAddress(),
                        event.getCoverImage()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventItemDTO> filterEventsAdmin(EventFilterDTO eventFilterDTO) {
        List<Event> eventsFiltered= eventRepo.findEventsByFiltersAdmin(
                eventFilterDTO.name(),
                eventFilterDTO.eventType(),
                eventFilterDTO.city()
        );
        return eventsFiltered.stream().
                map(event -> new EventItemDTO(
                        event.getName(),
                        event.getDate(),
                        event.getAddress(),
                        event.getCoverImage()
                )).collect(Collectors.toList());
    }
}
