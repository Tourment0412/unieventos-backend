package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.eventdtos.EventFilterDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventInfoDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventItemDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final EventService eventService;
    private  final OrderService orderService;

    @GetMapping("/event/get-all/{page}")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> listEventsClient(@PathVariable int page){
        List<EventItemDTO> events = eventService.listEventsClient(page);
        return ResponseEntity.ok(new MessageDTO<>(false,events));
    }

    @PostMapping("/event/filter-events")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> filterEventsClient(@Valid @RequestBody EventFilterDTO eventFilterDTO){
        List<EventItemDTO> events = eventService.filterEventsClient(eventFilterDTO);
        return ResponseEntity.ok(new MessageDTO<>(false,events));
    }

    //TODO (ask) Teacher has a controller to get just 1 event it is for the object? same as admin or it h
    // as to be just a basic info? I think is the DTO with the info
    @GetMapping("/event/get-info/{id}")
    public ResponseEntity<MessageDTO<EventInfoDTO>> getInfoEvenClient(@PathVariable String id) throws Exception{
        EventInfoDTO eventInfo = eventService.getInfoEventClient(id);
        return ResponseEntity.ok(new MessageDTO<>(false,eventInfo));
    }

    //TODO (ask) if this service method still should be void or return
    // and id of the order
    //TODO (Ask) if I can return a Map in the messageDTO (yeah bit how it's going to work or look).
    //TODO (Ask) why is it a post if it makes modifications on an order already existent

    @PostMapping("/order/receive-notification")
    public ResponseEntity<MessageDTO<String>> receiveNotificationFromMercadoPago(@RequestBody Map<String, Object> request){
        orderService.receiveNotificationFromMercadoPago(request);
        return ResponseEntity.ok(new MessageDTO<>(false,"Notification received"));
    }

}
