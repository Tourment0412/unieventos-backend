package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.coupondtos.CreateCouponDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderFilterDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderInfoDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderItemDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.AddShoppingCarDetailDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.repositories.OrderRepo;
import co.edu.uniquindio.unieventos.services.implementations.OrderServiceImp;
import co.edu.uniquindio.unieventos.services.interfaces.CouponSevice;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private CouponSevice couponService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ShoppingCarService shoppingCarService;


    private PreferenceClient preferenceClient; // Cliente real de MercadoPago

    @BeforeEach
    void setUp() {
       // orderRepo.deleteAll(); // Limpiar el repositorio antes de cada prueba
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Arrange
        CreateOrderDTO createOrderDTO = new CreateOrderDTO("66fadcb5d08fb70890780065", "66fafd1310b4027d916c95dd");

        // Crear un DTO de cupón con los datos requeridos
        //CreateCouponDTO createCouponDTO = new CreateCouponDTO(0.10f, LocalDateTime.now().plusDays(7), CouponType.UNIQUE, "3");
        //couponService.createCoupon(createCouponDTO);

        // Crear un carrito de compras con detalles
        ShoppingCar shoppingCar = shoppingCarService.getShoppingCar("66fadcb5d08fb70890780065");

        // Crear un DTO de detalle de carrito con los atributos necesarios: idUser, idEvent, locationName, quantity
        AddShoppingCarDetailDTO addShoppingCarDetailDTO = new AddShoppingCarDetailDTO("66fadcb5d08fb70890780065", "66f9e4d542ac3e6f6a3d7672", "Locación 1", 2);

        // Agregar el detalle del carrito utilizando el DTO
        shoppingCarService.addShoppingCarDetail(addShoppingCarDetailDTO);

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
        List<OrderItemDTO> orders = orderService.listOrdersCient("64f6d15801b1fc6c7c2037d4");

        // Verificar los resultados
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(order.getClientId().toString(), orders.get(0).clientId());
    }

    @Test
    void testFilterOrders() {
        // Crear una orden de prueba en la base de datos
        Order order = new Order();
        order.setId(ObjectId.get().toString());
        order.setClientId(new ObjectId("64f6d15801b1fc6c7c2037d4"));
        order.setDate(LocalDateTime.now());
        order.setItems(new ArrayList<OrderDetail>());
        order.setTotal(5000f);

        orderRepo.save(order);

        // Crear el filtro
        OrderFilterDTO filterDTO = new OrderFilterDTO(LocalDateTime.now());


        // Ejecutar el método a probar
        List<OrderItemDTO> filteredOrders = orderService.filterOrders(filterDTO);

        // Verificar los resultados
        assertNotNull(filteredOrders);
        // Aquí depende de cómo esté implementado el filtro
        assertEquals(0, filteredOrders.size());  // Si aún no has implementado el filtro, ajusta esta verificación.
    }


/*
    @Test
    public void testMakePayment() throws Exception {
        // Arrange
        // Crear un objeto PreferenceRequest para simular el pago
        PreferenceRequest preferenceRequest = new PreferenceRequest.Builder().build();

        // Crear una orden
        Order order = new Order();
        orderRepo.save(order);

        // Act
        Preference result = orderService.makePayment(order.getId());

        // Assert
        assertNotNull(result);
        assertEquals(result.getId(), preferenceClient.create(preferenceRequest).getId());

        // Verificar que la orden fue guardada después del pago
        Optional<Order> savedOrder = orderRepo.findById(order.getId());
        assertNotNull(savedOrder);
    }

 */
}

