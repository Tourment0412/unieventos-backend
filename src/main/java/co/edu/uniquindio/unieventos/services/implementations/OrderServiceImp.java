package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.orderdtos.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderFilterDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderInfoDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderItemDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.repositories.OrderRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.html.HTMLScriptElement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor=Exception.class)
public class OrderServiceImp implements OrderService {
    private final OrderRepo orderRepo;
    private final CouponRepo couponRepo;
    private final EventRepo eventRepo;
    private final EventServicesImp eventServicesImp;
    private final EventService eventService;

    public OrderServiceImp(OrderRepo orderRepo, CouponRepo couponRepo, EventRepo eventRepo, EventServicesImp eventServicesImp, EventService eventService) {
        this.orderRepo = orderRepo;
        this.couponRepo = couponRepo;
        this.eventRepo = eventRepo;
        this.eventServicesImp = eventServicesImp;
        this.eventService = eventService;
    }

    @Override
    public String createOrder(CreateOrderDTO createOrderDTO) throws Exception {


        Order order = new Order();

        order.setItems(createOrderDTO.items());
        //TODO code for gateway
        order.setGatewayCode("GATEWAYCODE");
        order.setDate(LocalDateTime.now());
        order.setTotal(calculateTotal(createOrderDTO.items(), createOrderDTO.couponId()));
        order.setClientId(new ObjectId(createOrderDTO.clientId()));
        order.setCouponId(new ObjectId(createOrderDTO.couponId()));

        //TODO Agregar Pago
        order.setPayment(new Payment());

        Order createOrder = orderRepo.save(order);
        return createOrder.getId();
    }

    private float calculateTotal(List<OrderDetail> items, String couponId) {
        float total = 0;
        Optional<Coupon> optionalCoupon= couponRepo.findByCode(couponId);
        for (OrderDetail orderDetail : items) {
            total+=orderDetail.getPrice();
        }
        return total-(total*optionalCoupon.get().getDiscount());
    }

    //TODO Remove this method, ID on Order should be the Mongodb ID
    private String createIdOrder() {
        String string = "ABCOEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * string.length());
            char character = string.charAt(index);

            result.append(character);
        }
        return result.toString();
    }


    private boolean existIdOrder(String s) {
        return orderRepo.findOrderById(s).isPresent();
    }


    private Order getOrder(String s) throws Exception {
        Optional<Order> orderOptional = orderRepo.findById(s);
        if (orderOptional.isEmpty()) {
            throw new Exception("The Order with the id: "+s+" does not exist");
        }
        return orderOptional.get();
    }

    @Override
    public String deleteOrder(String orderId) throws Exception {
         Order orderToDelete = getOrder(orderId);
         orderRepo.delete(orderToDelete);
         return "The order was deleted";
    }

    @Override
    public OrderInfoDTO getInfoOrder(String orderId) throws Exception {
        Order order=getOrder(orderId);

        return new OrderInfoDTO(
                order.getClientId().toString(),
                order.getDate(),
                order.getItems(),
                order.getPayment().getPaymentType(),
                order.getPayment().getStatus(),
                order.getPayment().getDate(),
                order.getPayment().getTransactionValue(),
                order.getId(),
                order.getTotal(),
                order.getCouponId().toString()
        );
    }

    @Override
    public List<OrderItemDTO> listOrdersAdmin() {
        List<Order> orders = orderRepo.findAll();
        return getOrderItemDTOS(orders);
    }

    @Override
    public List<OrderItemDTO> listOrdersCient(String cientId) throws Exception {
        ObjectId clientId = new ObjectId(cientId);

        List<Order> orders = orderRepo.findOrdersByClientId(clientId);

        return getOrderItemDTOS(orders);
    }

    @NotNull
    private List<OrderItemDTO> getOrderItemDTOS(List<Order> orders) {
        return orders.stream().map(order -> new OrderItemDTO(
                        order.getClientId() != null ? order.getClientId().toString() : null,
                        order.getDate(),
                        order.getItems(),
                        order.getPayment() != null ? order.getPayment().getPaymentType() : null,
                        order.getPayment() != null ? order.getPayment().getStatus() : null,
                        order.getPayment() != null ? order.getPayment().getDate() : null,
                        order.getPayment() != null ? order.getPayment().getTransactionValue() : 0f,
                        order.getId(),
                        order.getTotal(),
                        order.getCouponId() != null ? order.getCouponId().toString() : null
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDTO> filterOrders(OrderFilterDTO filterOrderDTO) {
        return List.of();
    }

    @Override
    public Preference makePayment(String idOrden) throws Exception {
        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Order saveOrder = getOrder(idOrden);
        List<PreferenceItemRequest> itemsGateway = new ArrayList<>();


        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(OrderDetail item : saveOrder.getItems()){


            // Obtener el evento y la localidad del ítem
            Event event = eventService.getEvent(item.getEventId().toString());
            Location location = event.getLocation(item.getLocationName());


            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(event.getId())
                            .title(event.getName())
                            .pictureUrl(event.getCoverImage())
                            .categoryId(event.getType().name())
                            .quantity(item.getQuantity())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(location.getPrice()))
                            .build();


            itemsGateway.add(itemRequest);
        }


        // Configurar las credenciales de MercadoPago
        MercadoPagoConfig.setAccessToken("ACCESS_TOKEN");


        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsGateway)
                .metadata(Map.of("id_orden", saveOrder.getId()))
                .notificationUrl("URL NOTIFICACION")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        saveOrder.setGatewayCode( preference.getId() );
        orderRepo.save(saveOrder);


        return preference;
    }

    @Override
    public void receiveNotificationFromMercadoPago(Map<String, Object> request) {
        try {


            // Obtener el tipo de notificación
            Object tipo = request.get("type");


            // Si la notificación es de un pago entonces obtener el pago y la orden asociada
            if ("payment".equals(tipo)) {


                // Capturamos el JSON que viene en el request y lo convertimos a un String
                String input = request.get("data").toString();


                // Extraemos los números de la cadena, es decir, el id del pago
                String idPago = input.replaceAll("\\D+", "");


                // Se crea el cliente de MercadoPago y se obtiene el pago con el id
                PaymentClient client = new PaymentClient();
                com.mercadopago.resources.payment.Payment payment = client.get( Long.parseLong(idPago) );


                // Obtener el id de la orden asociada al pago que viene en los metadatos
                String idOrden = payment.getMetadata().get("id_orden").toString();


                // Se obtiene la orden guardada en la base de datos y se le asigna el pago
                Order order = getOrder(idOrden);
                Payment orderPayment = createPayment(payment);
                order.setPayment(orderPayment);
                orderRepo.save(order);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Payment createPayment(com.mercadopago.resources.payment.Payment payment) {
        Payment orderPayment = new Payment();
        orderPayment.setId(payment.getId().toString());
        orderPayment.setDate( payment.getDateCreated().toLocalDateTime() );
        orderPayment.setStatus(payment.getStatus());
        orderPayment.setStatusDetail(payment.getStatusDetail());
        orderPayment.setPaymentType(payment.getPaymentTypeId());
        orderPayment.setCurrency(payment.getCurrencyId());
        orderPayment.setAuthorizationCode(payment.getAuthorizationCode());
        orderPayment.setTransactionValue(payment.getTransactionAmount().floatValue());
        return orderPayment;
    }

}
