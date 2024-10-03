package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.eventdtos.EventFilterDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventInfoDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventItemDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final EventService eventService;

    @GetMapping("/event/get-all/{page}")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> listEventsClient(@PathVariable int page){
        List<EventItemDTO> events = eventService.listEventsClient(page);
        return ResponseEntity.ok(new MessageDTO<>(false,events));
    }

    @GetMapping("/event/filter-events")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> filterEventsClient(@Valid @RequestBody EventFilterDTO eventFilterDTO){
        List<EventItemDTO> events = eventService.filterEventsClient(eventFilterDTO);
        return ResponseEntity.ok(new MessageDTO<>(true,events));
    }

    //TODO (ask) Teacher has a controller to get just 1 event it is for the object? same as admin or it h
    // as to be just a basic info? I think is the DTO with the info
    @GetMapping("/event/get-info/{id}")
    public ResponseEntity<MessageDTO<EventInfoDTO>> getInfoEvent(@PathVariable String id) throws Exception{
        EventInfoDTO eventInfo = eventService.getInfoEvent(id);
        return ResponseEntity.ok(new MessageDTO<>(true,eventInfo));
    }


    //TODO Here I need a controller to get a notification on Order
}
