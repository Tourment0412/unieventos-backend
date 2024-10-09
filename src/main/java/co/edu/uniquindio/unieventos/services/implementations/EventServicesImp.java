package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.dto.orderdtos.EventReportDTO;
import co.edu.uniquindio.unieventos.exceptions.DuplicateResourceException;
import co.edu.uniquindio.unieventos.exceptions.InsufficientCapacityException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
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
            throw new DuplicateResourceException("An event with this name is already active");
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

        return eventRepo.save(eventToUpdate).getId();
    }

    @Override
    public Event getEvent(String id) throws Exception {
        Optional<Event> eventToUpdate = eventRepo.findById(id);
        if (eventToUpdate.isEmpty()) {
            throw new ResourceNotFoundException("Event with this id does not exist");
        }
        return eventToUpdate.get();
    }


    @Override
    public String deleteEvent(String id) throws Exception {
        Event eventToDelete = getEvent(id);
        eventToDelete.setStatus(EventStatus.INACTIVE);


        return eventRepo.save(eventToDelete).getId();
    }

    @Override
    public EventInfoAdminDTO getInfoEventAdmin(String id) throws Exception {
        Event event = getEvent(id);
        return new EventInfoAdminDTO(
                event.getId(),
                event.getName(),
                event.getAddress(),
                event.getCoverImage(),
                event.getLocalitiesImage(),
                event.getDate(),
                event.getDescription(),
                event.getType(),
                event.getStatus(),
                event.getLocations()
        );

    }

    @Override
    public EventInfoDTO getInfoEventClient(String id) throws Exception {
        Optional<Event> eventGot = eventRepo.findEventByIdClient(id);
        if (eventGot.isEmpty()) {
            throw new ResourceNotFoundException("Event with this id does not exist");
        }
        Event event = eventGot.get();
        return new EventInfoDTO(
                event.getId(),
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
    public List<EventItemDTO> listEventsAdmin(int page) {
        //For the admin, all events are going to be shown.
        List<Event> events = eventRepo.findAll(PageRequest.of(page, 9)).getContent();

        return events.stream().map(event -> new EventItemDTO(
                event.getId(),
                event.getName(),
                event.getDate(),        // Asegúrate de que sea LocalDateTime si es necesario
                event.getAddress(),
                event.getCoverImage()
        )).collect(Collectors.toList());
    }

    @Override
    public List<EventItemDTO> listEventsClient(int page) {
        //listar excluyendo los inactivos y los vencidos
        List<Event> events = eventRepo.findAllEventsClient(PageRequest.of(page, 9)).getContent();
        return events.stream()
                .map(event -> new EventItemDTO(
                        event.getId(),
                        event.getName(),
                        event.getDate(),
                        event.getAddress(),
                        event.getCoverImage()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public List<EventItemDTO> filterEventsClient(EventFilterDTO eventFilterDTO) {
        Map<String, Object> params = createFilterMap(eventFilterDTO);


        List<Event> eventsFiltered= eventRepo.findEventsByFiltersClient(params,PageRequest.of(0, 9)).getContent();
        return eventsFiltered.stream()
                .map(event -> new EventItemDTO(
                        event.getId(),
                        event.getName(),
                        event.getDate(),
                        event.getAddress(),
                        event.getCoverImage()
                ))
                .collect(Collectors.toList());

    }

    private Map<String, Object> createFilterMap(EventFilterDTO eventFilterDTO) {
        Map <String, Object> params = new HashMap<>();
        if (eventFilterDTO.name() != null && !eventFilterDTO.name().isEmpty()) {
            String regexName = ".*" + eventFilterDTO.name() + ".*";
            params.put("name", Pattern.compile(regexName, Pattern.CASE_INSENSITIVE));
        }
        if(eventFilterDTO.eventType()!=null){
            String regexType = ".*" + eventFilterDTO.eventType() + ".*";
            params.put("type", Pattern.compile(regexType, Pattern.CASE_INSENSITIVE));
        }
        if(eventFilterDTO.city() != null && !eventFilterDTO.city().isEmpty()){
            String regexCity = ".*" + eventFilterDTO.city() + ".*";
            params.put("city", Pattern.compile(regexCity, Pattern.CASE_INSENSITIVE));
        }
        return params;
    }

    @Override
    public List<EventItemDTO> filterEventsAdmin(EventFilterDTO eventFilterDTO) {

        Map<String,Object>params= createFilterMap(eventFilterDTO);

        List<Event> eventsFiltered= eventRepo.findEventsByFiltersAdmin(params,PageRequest.of(0, 9)).getContent();
        return eventsFiltered.stream().map(event -> new EventItemDTO(
                event.getId(),
                event.getName(),
                event.getDate(),
                event.getAddress(),
                event.getCoverImage()
        )).collect(Collectors.toList());

    }

    @Override
    public void reduceNumberLocations(int numLocations, String nameLocation, String idEvent) throws Exception {
        Event event = getEvent(idEvent);
        Location location = event.findLocationByName(nameLocation);
        if (location == null) {
            throw new ResourceNotFoundException("Location " + nameLocation + " does not exist");
        } else if (location.getTicketsSold() + numLocations > location.getMaxCapacity()) {
            throw new InsufficientCapacityException("Location " + nameLocation + " is too high");
        }
        location.setTicketsSold(location.getTicketsSold() + numLocations);
        eventRepo.save(event);
    }

    @Override
    public EventReportDTO createReport(String idEvent) {
        Map<String, Double> percentageSoldByLocation = eventRepo.calculatePercentageSoldByLocation(idEvent);
        Map<String, Integer> quantitySoldByLocation = eventRepo.calculateQuantitySoldByLocation(idEvent);
        Map<String, Double> soldByLocation = eventRepo.calculateSoldByLocation(idEvent);
        int totalTickets = 0;
        double totalSales = 0.0;


        if (soldByLocation != null) {
            for (Double sales : soldByLocation.values()) {
                totalSales += sales;
            }
        }

        if (quantitySoldByLocation != null) {
            for (Integer tickets : quantitySoldByLocation.values()) {
                totalTickets += tickets;
            }
        }

        if (soldByLocation == null) soldByLocation = new HashMap<>();
        if (percentageSoldByLocation == null) percentageSoldByLocation = new HashMap<>();
        if (quantitySoldByLocation == null) quantitySoldByLocation = new HashMap<>();

        return new EventReportDTO(
                soldByLocation,
                percentageSoldByLocation,
                quantitySoldByLocation,
                totalSales,
                totalTickets
        );
    }

}
