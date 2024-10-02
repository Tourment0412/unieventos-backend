package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.eventdtos.EventFilterDTO;
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

    @GetMapping("/public/get-all/{page}")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> listEventsClient(@PathVariable int page){
        List<EventItemDTO> events = eventService.listEventsClient(page);
        return ResponseEntity.ok(new MessageDTO<>(false,events));
    }

    @GetMapping("/public/filter-events")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> filterEventsClient(@Valid @RequestBody EventFilterDTO eventFilterDTO){
        List<EventItemDTO> events = eventService.filterEventsClient(eventFilterDTO);
        return ResponseEntity.ok(new MessageDTO<>(true,events));
    }
}
