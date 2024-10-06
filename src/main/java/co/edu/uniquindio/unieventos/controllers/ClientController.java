package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.coupondtos.CouponInfoClientDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CouponItemClientDTO;
import co.edu.uniquindio.unieventos.dto.coupondtos.CouponItemDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.AddShoppingCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.CarItemViewDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.DeleteCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.EditCarDetailDTO;
import co.edu.uniquindio.unieventos.services.interfaces.CouponService;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {

    /*
        This is the controller for the actions a Client account can do in the web

        It has to do with the order and the shopping car and the shopping order
     */

    private final ShoppingCarService shoppingCarService;
    private final OrderService orderService;
    private final CouponService couponService;

    @PutMapping("/shoppingcar/add-item")
    public ResponseEntity<MessageDTO<String>> addShoppingCarDetail
            (@Valid @RequestBody AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws Exception{
        shoppingCarService.addShoppingCarDetail(addShoppingCarDetailDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Shopping car item added successfully"));
    }

    @PutMapping("/shoppingcar/edit-item")
    public ResponseEntity<MessageDTO<String>> editCarDetail (@Valid @RequestBody EditCarDetailDTO editCarDetailDTO) throws Exception{
        shoppingCarService.editCarDetail(editCarDetailDTO);
        return ResponseEntity.ok(new MessageDTO<>(true, "Car item edited successfully"));
    }

    @DeleteMapping("/shoppingcar/delete-item")
    public ResponseEntity<MessageDTO<String>> deleteShoppingCarDetail(@Valid @RequestBody DeleteCarDetailDTO deleteCarDetailDTO) throws Exception{
        shoppingCarService.deleteShoppingCarDetail(deleteCarDetailDTO);
        return ResponseEntity.ok(new MessageDTO<>(true, "Shopping car item deleted successfully"));
    }

    @PostMapping("/shoppingcar/create/{idUser}")
    public ResponseEntity<MessageDTO<String>> createShoppingCar(@PathVariable String idUser) throws Exception{
        shoppingCarService.createShoppingCar(idUser);
        return ResponseEntity.ok(new MessageDTO<>(true, "Shopping car created successfully"));
    }

    //TODO Ask if the carrito obtener controller is for the list of the items or to get the entity of Shopping car
    //I'm gonna do it as it's for the list of the items
    @GetMapping("/shoppingcar/get-items/{idUser}")
    public ResponseEntity<MessageDTO<List<CarItemViewDTO>>> listShoppingCarDetails(@PathVariable String idUser) throws Exception{
        List<CarItemViewDTO> carItems = shoppingCarService.listShoppingCarDetails(idUser);
        return ResponseEntity.ok(new MessageDTO<>(false, carItems));
    }



    //TODO (Ask) to the teacher why his method has a DTO for this and not just the order id (And why this is post)
    @PostMapping("/order/make-payment/{idOrden}")
    public ResponseEntity<MessageDTO<PaymentResponseDTO>> makePayment(@PathVariable String idOrden) throws Exception {
        // Llamamos al método del servicio para crear el
        PaymentResponseDTO paymentResponse = orderService.makePayment(idOrden);
        return ResponseEntity.ok(new MessageDTO<>(false, paymentResponse));
    }

    @PostMapping("/order/create")
    public ResponseEntity<MessageDTO<String>> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) throws Exception{
        orderService.createOrder(createOrderDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Order created successfully"));
    }

    @GetMapping("/order/history/{clientId}")
    public ResponseEntity<MessageDTO<List<OrderItemDTO>>> listOrdersClient(@PathVariable String clientId) throws Exception{
        List<OrderItemDTO> orders= orderService.listOrdersClient(clientId);
        return ResponseEntity.ok(new MessageDTO<>(false, orders));
    }

    @GetMapping("/order/get-info/{orderId}")
    public ResponseEntity<MessageDTO<OrderInfoDTO>> getInfoOrder(@PathVariable String orderId) throws Exception{
        OrderInfoDTO orderInfo = orderService.getInfoOrder(orderId);
        return ResponseEntity.ok(new MessageDTO<>(false, orderInfo));
    }

    @GetMapping("/order/filter-orders")
    public ResponseEntity<MessageDTO<List<OrderItemDTO>>> filterOrders(@Valid @RequestBody OrderFilterDTO filterOrderDTO){
        List<OrderItemDTO> orders = orderService.filterOrders(filterOrderDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, orders));
    }


    //Coupon methods for the Clients (Here is going to be our EXTRA FUNCTIONALITY

    //TODO Ask if this could be the tittle for the extra functionality.
    @GetMapping("/coupon/get-all/{page}")
    public ResponseEntity<MessageDTO<List<CouponItemClientDTO>>> couponsAvailable(@PathVariable int page) throws Exception {
        List<CouponItemClientDTO> coupons= couponService.getAllCouponsClient(page);
        return ResponseEntity.ok(new MessageDTO<>(false, coupons));
    }
    //TODO ask if it does not matter that the functionality that is to give away the purchase is done as an
    // implementation in the service and with the two new attributes of the order, and in the front end it will
    // be seen as a separate window.

    @GetMapping("/coupon/get-info/{id}")
    public ResponseEntity<MessageDTO<CouponInfoClientDTO>> getCouponClient(@PathVariable String id) throws Exception{
        CouponInfoClientDTO coupon= couponService.getCouponClient(id);
        return ResponseEntity.ok(new MessageDTO<>(false, coupon));
    }










}
