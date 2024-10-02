package co.edu.uniquindio.unieventos.controllers;


import co.edu.uniquindio.unieventos.dto.coupondtos.CreateCouponDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventFilterDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.EventItemDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.UpdateEventDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.services.interfaces.CouponSevice;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.ImagesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final EventService eventService;
    private final CouponSevice couponSevice;
    private final ImagesService imagesService;

    @PutMapping("/event/update")
    public ResponseEntity<MessageDTO<String>> updateEvent(@Valid @RequestBody UpdateEventDTO updateEventDTO) throws Exception {
        eventService.updateEvent(updateEventDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Event updated successfully"));
    }

    @PostMapping("/image/upload")
    public ResponseEntity<MessageDTO<String>> uploadImage(@RequestParam("image") MultipartFile image) throws Exception {
        String answer = imagesService.uploadImage(image);
        return ResponseEntity.ok().body(new MessageDTO<>(false, answer));
    }


    //In the front end I'm going to be able to create an event if I already selected the images
    @PostMapping("/event/create")
    public ResponseEntity<MessageDTO<String>> createEvent(@Valid @RequestBody CreateEventDTO createEventDTO) throws Exception {
        eventService.createEvent(createEventDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Event created successfully"));
    }

    @PostMapping("/coupon/create")
    public ResponseEntity<MessageDTO<String>> createCoupon(@Valid @RequestBody CreateCouponDTO coupon) throws Exception {
        couponSevice.createCoupon(coupon);
        return ResponseEntity.ok(new MessageDTO<>(false, "Coupon created successfully"));
    }

    //TODO ask if this GetEvent controller should be this one where I get the whole entity or just the info
    @GetMapping("/event/get/{id}")
    public ResponseEntity<MessageDTO<Event>> getEvent(@PathVariable String id) throws Exception {
        Event event = eventService.getEvent(id);
        return ResponseEntity.ok(new MessageDTO<>(false, event));
    }

    //TODO Ask if this method doesn't have a exception explicitly thrown in the method, it will just return exception
    // related with the mapping, the page or the query
    @GetMapping("/event/get-all/{page}")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> listEventsAdmin(@PathVariable int page) {
        List<EventItemDTO> events = eventService.listEventsAdmin(page);
        return ResponseEntity.ok(new MessageDTO<>(false, events));
    }


    //TODO ask if this GetCoupon controller should be this one where I get the whole entity
    @GetMapping("/coupon/get/{id}")
    public ResponseEntity<MessageDTO<Coupon>> getCouponById(@PathVariable String id) throws Exception {
        Coupon coupon = couponSevice.getCouponById(id);
        return ResponseEntity.ok(new MessageDTO<>(false, coupon));
    }

    //TODO I need a service to get all coupons for the admin


    @DeleteMapping("/image/delete")
    public ResponseEntity<MessageDTO<String>> deleteImage(@RequestParam("idImage") String idImage) throws Exception {
        imagesService.deleteImage(idImage);
        return ResponseEntity.ok().body(new MessageDTO<>(false, "The image was successfully deleted"));
    }

    @DeleteMapping("/event/delete/{id}")
    public ResponseEntity<MessageDTO<String>> deleteEvent(@PathVariable String id) throws Exception {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().body(new MessageDTO<>(false, "The event was successfully deleted"));
    }

    @DeleteMapping("/coupon/delete/{id}")
    public ResponseEntity<MessageDTO<String>> deleteCoupon(@PathVariable String id) throws Exception{
        couponSevice.deleteCoupon(id);
        return ResponseEntity.ok().body(new MessageDTO<>(false, "Coupon was successfully deleted"));
    }

    @GetMapping("/event/filter-events")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> filterEventsAdmin(@Valid @RequestBody EventFilterDTO eventFilterDTO){
        List<EventItemDTO> eventsFiltered= eventService.filterEventsAdmin(eventFilterDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, eventsFiltered));
    }


}
