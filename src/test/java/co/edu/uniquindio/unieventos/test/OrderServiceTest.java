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

    private String eventId="67019b1d63e5b8567aabf871";
    private String clientId="66faf8347e0c1e7206761d25";
    private String couponId="66fafd1310b4027d916c95dd";

    @BeforeEach
    void setUp() {
       // orderRepo.deleteAll(); // Limpiar el repositorio antes de cada prueba
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Arrange
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(
                false,
                "",
                clientId,
                "MULTI5"
                );

        // Crear un DTO de cupón con los datos requeridos
        //CreateCouponDTO createCouponDTO = new CreateCouponDTO(0.10f, LocalDateTime.now().plusDays(7), CouponType.UNIQUE, "3");
        //couponService.createCoupon(createCouponDTO);

        // Crear un carrito de compras con detalles
        //ShoppingCar shoppingCar = shoppingCarService.getShoppingCar(clientId);

        // Crear un DTO de detalle de carrito con los atributos necesarios: idUser, idEvent, locationName, quantity
        //AddShoppingCarDetailDTO addShoppingCarDetailDTO = new AddShoppingCarDetailDTO("66fadcb5d08fb70890780065", "66f9e4d542ac3e6f6a3d7672", "Locación 1", 2);

        // Agregar el detalle del carrito utilizando el DTO
        //shoppingCarService.addShoppingCarDetail(addShoppingCarDetailDTO);

        // Act
        String orderId = orderService.createOrder(createOrderDTO);

        // Assert
        assertNotNull(orderId);
        Optional<Order> savedOrder = orderRepo.findById(orderId);
        assertEquals(savedOrder.get().getId(), orderId);
    }



    @Test
    public void testDeleteOrder() throws Exception {
        // Arrange
        Order order = new Order();
        orderRepo.save(order); // Guardar orden en la base de datos

        // Act
        String response = orderService.deleteOrder(order.getId());

        // Assert
        assertEquals("The order was deleted", response);

        // Verificar que la orden fue eliminada
        Optional<Order> deletedOrder = orderRepo.findById(order.getId());
        assertEquals(Optional.empty(), deletedOrder);
    }

    @Test
    void testGetInfoOrder() throws Exception {
        // Crear y guardar una orden de prueba en la base de datos
        Order order = new Order();
        order.setId(ObjectId.get().toString());
        order.setClientId(new ObjectId("64f6d15801b1fc6c7c2037d4"));
        order.setDate(LocalDateTime.now());
        order.setItems(new ArrayList<OrderDetail>());
        order.setTotal(5000f);

        orderRepo.save(order);

        // Ejecutar el método a probar
        OrderInfoDTO orderInfo = orderService.getInfoOrder(order.getId());

        // Verificar que los resultados sean los esperados
        assertNotNull(orderInfo);
        assertEquals(order.getId(), orderInfo.id());
        assertEquals(order.getClientId().toString(), orderInfo.clientId());
    }

    @Test
    void testListOrdersCient() throws Exception {
        // Crear y guardar una orden de prueba asociada a un cliente
        Order order = new Order();
        order.setId(ObjectId.get().toString());
        order.setClientId(new ObjectId("64f6d15801b1fc6c7c2037d4"));
        order.setDate(LocalDateTime.now());
        order.setItems(new ArrayList<OrderDetail>());
        order.setTotal(5000f);

        orderRepo.save(order);

        // Ejecutar el método a probar
        List<OrderItemDTO> orders = orderService.listOrdersClient("64f6d15801b1fc6c7c2037d4");

        // Verificar los resultados
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(order.getClientId().toString(), orders.get(0).clientId());
    }

/*
    @Test
    public void testGenerateEventReport() {

        List<Location> locations = new ArrayList<>();
        locations.add(new Location("VIP", 100)); // Ejemplo de una ubicación con 100 asientos
        locations.add(new Location("General", 200)); // Ejemplo de una ubicación con 200 asientos

        Event event = new Event(eventId, "Concierto de Prueba", LocalDateTime.now(), locations);

        List<OrderDetail> orderDetails1 = new ArrayList<>();
        orderDetails1.add(new OrderDetail(eventId, "VIP", 2, 100.0)); // 2 boletos vendidos para VIP a 100.0 cada uno
        orderDetails1.add(new OrderDetail(eventId, "General", 3, 50.0)); // 3 boletos vendidos para General a 50.0 cada uno

        List<OrderDetail> orderDetails2 = new ArrayList<>();
        orderDetails2.add(new OrderDetail(eventId, "General", 5, 50.0)); // 5 boletos vendidos para General a 50.0 cada uno

        List<Order> orders = new ArrayList<>();
        orders.add(new Order("order1", "client1", LocalDateTime.now(), orderDetails1));
        orders.add(new Order("order2", "client2", LocalDateTime.now(), orderDetails2));


        // Generar el reporte
        EventReportDTO report = orderService.generateEventReport("event123");

        // Verificar el contenido del reporte
        assertEquals("event123", report.eventId());
        assertEquals("Concierto de Prueba", report.eventName());
        assertNotNull(report.eventDate());

        // Verificar estadísticas de ventas por ubicación
        assertEquals(2, report.soldByLocation().get("VIP"));
        assertEquals(8, report.soldByLocation().get("General")); // 3 + 5 boletos vendidos en General

        // Verificar porcentaje vendido
        assertEquals(2 * 100.0 / 100, report.percentageSoldByLocation().get("VIP")); // 2 de 100
        assertEquals(8 * 100.0 / 200, report.percentageSoldByLocation().get("General")); // 8 de 200

        // Verificar ventas totales
        assertEquals(new BigDecimal("650.0"), report.totalSales()); // 2 * 100.0 + 3 * 50.0 + 5 * 50.0

        // Verificar total de boletos disponibles
        assertEquals(new BigDecimal("300"), report.totalTickets()); // 100 VIP + 200 General
    }

 */

    @Test
    void testSendSummaryPurchase() throws Exception {
        Order order = orderService.getOrder("66fafe651fc0e1480e7822af");
        String message = orderService.sendPurchaseSummary("miraortega2020@gmail.com", order);


    }

}



