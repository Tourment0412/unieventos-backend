package co.edu.uniquindio.unieventos.controllers;


import co.edu.uniquindio.unieventos.dto.coupondtos.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CouponItemDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CreateCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.UpdateCouponDTO;
import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.services.interfaces.CouponService;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.ImagesService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")

//TODO Mirar bien como funcionan estas cosas con el Swagger
@SecurityRequirement(name = "bearerAuth")

public class AdminController {

    private final EventService eventService;
    private final CouponService couponService;
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
        couponService.createCoupon(coupon);
        return ResponseEntity.ok(new MessageDTO<>(false, "Coupon created successfully"));
    }

    @PutMapping("/coupon/update")
    public ResponseEntity<MessageDTO<String>> updateCoupon(@Valid @RequestBody UpdateCouponDTO coupon) throws Exception{
        couponService.updateCoupon(coupon);
        return ResponseEntity.ok(new MessageDTO<>(false, "Coupon updated successfully"));
    }


    @GetMapping("/event/get/{id}")
    public ResponseEntity<MessageDTO<EventInfoAdminDTO>> getEvent(@PathVariable String id) throws Exception {
        EventInfoAdminDTO event = eventService.getInfoEventAdmin(id);
        return ResponseEntity.ok(new MessageDTO<>(false, event));
    }


    @GetMapping("/event/get-all/{page}")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> listEventsAdmin(@PathVariable int page) {
        List<EventItemDTO> events = eventService.listEventsAdmin(page);
        return ResponseEntity.ok(new MessageDTO<>(false, events));
    }


    //TODO ask if this GetCoupon controller should be this one where I get the whole entity
    @GetMapping("/coupon/get/{id}")
    public ResponseEntity<MessageDTO<CouponInfoDTO>> getInfoCoupon(@PathVariable String id) throws Exception {
        CouponInfoDTO coupon = couponService.getInfoCouponAdmin(id);
        return ResponseEntity.ok(new MessageDTO<>(false, coupon));
    }

    //TODO I need a service to get all coupons for the admin
    @GetMapping("/coupon/get-all/{page}")
    public ResponseEntity<MessageDTO<List<CouponItemDTO>>> getAllCouponsAdmin(@PathVariable int page) throws Exception{
        List<CouponItemDTO> coupons = couponService.getAllCouponsAdmin(page);
        return ResponseEntity.ok(new MessageDTO<>(false, coupons));
    }


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
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().body(new MessageDTO<>(false, "Coupon was successfully deleted"));
    }

    @GetMapping("/event/filter-events")
    public ResponseEntity<MessageDTO<List<EventItemDTO>>> filterEventsAdmin(@Valid @RequestBody EventFilterDTO eventFilterDTO){
        List<EventItemDTO> eventsFiltered= eventService.filterEventsAdmin(eventFilterDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, eventsFiltered));
    }


}
