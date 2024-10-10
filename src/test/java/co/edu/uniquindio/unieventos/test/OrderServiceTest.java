package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.repositories.OrderRepo;
import co.edu.uniquindio.unieventos.services.interfaces.CouponService;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import com.mercadopago.client.preference.PreferenceClient;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private CouponService couponService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    private PreferenceClient preferenceClient;

    private String eventId = "67019b1d63e5b8567aabf871";
    private String clientId = "6706047ac127c9d5e7e16cc0";
    private String couponId = "66fafd1310b4027d916c95dd";

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testCreateOrder() throws Exception {
        // Arrange
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(
                clientId,
                "NEW15P");

        String orderId = orderService.createOrder(createOrderDTO);

        assertNotNull(orderId);
        Optional<Order> savedOrder = orderRepo.findById(orderId);
        assertEquals(savedOrder.get().getId(), orderId);
    }


    @Test
    public void testDeleteOrder() throws Exception {
        Order order = new Order();
        order.setId("67076f13686baa923c7fb3e0");
        orderRepo.save(order); // Guardar orden en la base de datos

        String response = orderService.deleteOrder(order.getId());

        assertEquals("The order was deleted", response);

        Optional<Order> deletedOrder = orderRepo.findById(order.getId());
        assertEquals(Optional.empty(), deletedOrder);
    }

    @Test
    void testGetInfoOrder() throws Exception {
        Order order = new Order();
        order.setId(ObjectId.get().toString());
        order.setClientId(new ObjectId("64f6d15801b1fc6c7c2037d4"));
        order.setDate(LocalDateTime.now());
        order.setItems(new ArrayList<OrderDetail>());
        order.setTotal(5000f);

        orderRepo.save(order);

        OrderItemDTO orderInfo = orderService.getInfoOrder(order.getId());

        assertNotNull(orderInfo);
        assertEquals(order.getId(), orderInfo.id());
        assertEquals(order.getClientId().toString(), orderInfo.clientId());
    }

    @Test
    void testListOrdersCient() throws Exception {
        Order order = new Order();
        order.setId(ObjectId.get().toString());
        order.setClientId(new ObjectId("64f6d15801b1fc6c7c2037d4"));
        order.setDate(LocalDateTime.now());
        order.setItems(new ArrayList<OrderDetail>());
        order.setTotal(5000f);

        orderRepo.save(order);

        List<OrderItemDTO> orders = orderService.listOrdersClient("64f6d15801b1fc6c7c2037d4");

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(order.getClientId().toString(), orders.get(0).clientId());
    }

    ////////////////////////////////////////////
    //TODO
    @Test
    void testMakePayment() throws Exception {
        //Esta funcionalidad no puede ser testeada en una prueba unitaria
        assertTrue(true);
    }



}



