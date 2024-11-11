package co.edu.uniquindio.unieventos.controllers;


import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.dto.eventdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.EventReportDTO;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
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
       String eventId= eventService.updateEvent(updateEventDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, eventId));
    }

    @PostMapping("/image/upload")
    public ResponseEntity<MessageDTO<String>> uploadImage(@RequestParam("image") MultipartFile image) throws Exception {
        String answer = imagesService.uploadImage(image);
        return ResponseEntity.ok().body(new MessageDTO<>(false, answer));
    }


    //In the front end I'm going to be able to create an event if I already selected the images
    @PostMapping("/event/create")
    public ResponseEntity<MessageDTO<String>> createEvent(@Valid @RequestBody CreateEventDTO createEventDTO) throws Exception {
        String eventId= eventService.createEvent(createEventDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, eventId));
    }

    @PostMapping("/coupon/create")
    public ResponseEntity<MessageDTO<String>> createCoupon(@Valid @RequestBody CreateCouponDTO coupon) throws Exception {
        String couponId= couponService.createCoupon(coupon);
        return ResponseEntity.ok(new MessageDTO<>(false, couponId));
    }

    @PutMapping("/coupon/update")
    public ResponseEntity<MessageDTO<String>> updateCoupon(@Valid @RequestBody UpdateCouponDTO coupon) throws Exception{
        String couponId= couponService.updateCoupon(coupon);
        return ResponseEntity.ok(new MessageDTO<>(false, couponId));
    }


    @GetMapping("/event/get/{id}")
    public ResponseEntity<MessageDTO<EventInfoAdminDTO>> getEvent(@PathVariable String id) throws Exception {
        EventInfoAdminDTO event = eventService.getInfoEventAdmin(id);
        return ResponseEntity.ok(new MessageDTO<>(false, event));
    }


    @GetMapping("/event/get-all/{page}")
    public ResponseEntity<MessageDTO<ListEvents>> listEventsAdmin(@PathVariable int page) {
        ListEvents events = eventService.listEventsAdmin(page);
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
    public ResponseEntity<MessageDTO<ListCoupon>> getAllCouponsAdmin(@PathVariable int page) throws Exception{
        ListCoupon coupons = couponService.getAllCouponsAdmin(page);
        return ResponseEntity.ok(new MessageDTO<>(false, coupons));
    }


    @DeleteMapping("/image/delete")
    public ResponseEntity<MessageDTO<String>> deleteImage(@RequestParam("idImage") String idImage) throws Exception {
        String reply= imagesService.deleteImage(idImage);
        return ResponseEntity.ok().body(new MessageDTO<>(false, reply));
    }

    @DeleteMapping("/event/delete/{id}")
    public ResponseEntity<MessageDTO<String>> deleteEvent(@PathVariable String id) throws Exception {
        String eventId= eventService.deleteEvent(id);
        return ResponseEntity.ok().body(new MessageDTO<>(false, eventId));
    }

    @DeleteMapping("/coupon/delete/{id}")
    public ResponseEntity<MessageDTO<String>> deleteCoupon(@PathVariable String id) throws Exception{
        String couponId= couponService.deleteCoupon(id);
        return ResponseEntity.ok().body(new MessageDTO<>(false, couponId));
    }

    @PostMapping("/event/filter-events")
    public ResponseEntity<MessageDTO<ListEvents>> filterEventsAdmin(@Valid @RequestBody EventFilterDTO eventFilterDTO){
        System.out.println("Entro");
        ListEvents eventsFiltered= eventService.filterEventsAdmin(eventFilterDTO);
        System.out.println("Salio");
        System.out.println(eventsFiltered);
        return ResponseEntity.ok(new MessageDTO<>(false, eventsFiltered));
    }


    //TODO Ask if here this should have a exception coming from the service
    @GetMapping("/event/reports-info/{idEvent}")
    public ResponseEntity<MessageDTO<EventReportDTO>> createReport(@PathVariable String idEvent){
        EventReportDTO report= eventService.createReport(idEvent);
        return ResponseEntity.ok().body(new MessageDTO<>(false, report));
    }

    @GetMapping("/coupon/get-types")
    public ResponseEntity<MessageDTO<List<CouponType>>>  getCouponTypes() throws Exception{
        List<CouponType> couponTypes= couponService.getCouponTypes();
        return ResponseEntity.ok().body(new MessageDTO<>(false, couponTypes));
    }

    @GetMapping("/coupon/get-statuses")
    public ResponseEntity<MessageDTO<List<CouponStatus>>> getCouponStatuses() throws Exception{
        List<CouponStatus> couponStatuses= couponService.getCouponStatuses();
        return ResponseEntity.ok().body(new MessageDTO<>(false, couponStatuses));
    }


}
