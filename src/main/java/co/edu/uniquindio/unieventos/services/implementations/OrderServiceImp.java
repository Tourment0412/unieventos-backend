package co.edu.uniquindio.unieventos.services.implementations;


import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.model.documents.*;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.model.vo.*;
import co.edu.uniquindio.unieventos.repositories.*;
import co.edu.uniquindio.unieventos.services.interfaces.*;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImp implements OrderService {
    private final CouponService couponService;
    private final EventService eventService;
    private final ShoppingCarService shoppingCarService;
    private final EmailService emailService;
    private final AccountService accountService;
    private final OrderRepo orderRepo;


    public OrderServiceImp(AccountService accountService, CouponService couponService, EventService eventService, ShoppingCarService shoppingCarService, EmailService emailService, OrderRepo orderRepo) {
        this.accountService = accountService;
        this.couponService = couponService;
        this.eventService = eventService;
        this.shoppingCarService = shoppingCarService;
        this.emailService = emailService;

        this.orderRepo = orderRepo;
    }

    @Override
    public String createOrder(CreateOrderDTO createOrderDTO) throws Exception {
        Order order = new Order();
        ShoppingCar shoppingCar = shoppingCarService.getShoppingCar(createOrderDTO.clientId());
        List<OrderDetail> items = getOrderDetails(shoppingCar);

        order.setItems(items);
        order.setDate(LocalDateTime.now());

        order.setClientId(new ObjectId(createOrderDTO.clientId()));
        if(!(createOrderDTO.counponCode()==null || createOrderDTO.counponCode().isEmpty())){
            Coupon coupon = couponService.getCouponByCode(createOrderDTO.counponCode());
            order.setTotal(calculateTotal(items, coupon.getId(), createOrderDTO.clientId()));
            order.setCouponId(new ObjectId(coupon.getId()));
        }else {
            order.setTotal(calculateTotal(items));
        }
        order.setGift(false);

        if(createOrderDTO.isForFriend()){
            accountService.getAccountEmail(createOrderDTO.friendEmail());
            order.setGift(true);
            order.setFriendMail(createOrderDTO.friendEmail());
            //TODO enviar entrada a ammigo
        }

        Account account = accountService.getAccount(createOrderDTO.clientId());
        Order createOrder = orderRepo.save(order);
        sendPurchaseSummary(account.getEmail(), order);
        shoppingCarService.deleteShoppingCar(createOrderDTO.clientId());
        return createOrder.getId();
    }

    private @NotNull List<OrderDetail> getOrderDetails(ShoppingCar shoppingCar) {
        List<OrderDetail> items = new ArrayList<>();
        List<CarDetail> details = shoppingCar.getItems();
        details.forEach(carDetail -> {
            try {

                Event event = eventService.getEvent(String.valueOf(carDetail.getIdEvent()));
                Location location = event.findLocationByName(carDetail.getLocationName());
                if (!location.isCapacityAvaible(carDetail.getAmount())) {
                    throw new Exception("Max capacity exceeded");
                } else if (event.getDate().minusDays(2).isBefore(LocalDateTime.now())) {
                    throw new Exception("Date is before current date");
                } else {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setEventId(carDetail.getIdEvent());
                    orderDetail.setLocationName(carDetail.getLocationName());
                    orderDetail.setPrice(carDetail.getAmount() * location.getPrice());
                    orderDetail.setQuantity(carDetail.getAmount());
                    items.add(orderDetail);

                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });
        return items;
    }

    private float calculateTotal(List<OrderDetail> items) {
        float total = 0;
        for (OrderDetail orderDetail : items) {
            total += orderDetail.getPrice();
        }
        return total;
    }

    private float calculateTotal(List<OrderDetail> items, String couponId, String idClient) throws Exception {
        float total = 0;
        Coupon coupon = couponService.getCouponById(couponId);
        if(coupon.getStatus().equals(CouponStatus.NOT_AVAILABLE)){
            throw new Exception("Coupon is not available");
        }
        for (OrderDetail orderDetail : items) {
            total += orderDetail.getPrice();
        }
        if (coupon.getType().equals(CouponType.UNIQUE)) {
            couponService.deleteCoupon(couponId);
        } else if (coupon.getType().equals(CouponType.MULTIPLE)) {
            List<Order> ordersClient=getOrdersByIdCliente(idClient);
            for (Order order : ordersClient) {
                if (coupon.getId().equals(order.getCouponId().toString())) {
                    throw new Exception("Coupon is already in use by this client");
                }
            }
        }
        return total - (total * coupon.getDiscount());
    }

    private List<Order> getOrdersByIdCliente(String idClient) {
        return orderRepo.findOrdersByClientId(new ObjectId(idClient));
    }


    private Order getOrder(String s) throws Exception {
        Optional<Order> orderOptional = orderRepo.findById(s);
        if (orderOptional.isEmpty()) {
            throw new Exception("The Order with the id: " + s + " does not exist");
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
        Order order = getOrder(orderId); // Método que obtiene la orden

        String couponId = (order.getCouponId() != null) ? order.getCouponId().toString() : null;

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
                couponId
        );
    }


    @Override
    public List<OrderItemDTO> listOrdersClient(String idClient){
        ObjectId clientId = new ObjectId(idClient);

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
    public PaymentResponseDTO makePayment(String idOrden) throws Exception {
        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Order saveOrder = getOrder(idOrden);
        List<PreferenceItemRequest> itemsGateway = new ArrayList<>();


        // Recorrer los items de la orden y crea los ítems de la pasarela
        for (OrderDetail item : saveOrder.getItems()) {


            // Obtener el evento y la localidad del ítem
            Event event = eventService.getEvent(item.getEventId().toString());
            Location location = event.findLocationByName(item.getLocationName());


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


        //TODO Configurar las credenciales de MercadoPag. Crear cuenta de mercado pago
        MercadoPagoConfig.setAccessToken("APP_USR-8178646482281064-100513-248819fc76ea7f7577f902e927eaefb7-2014458486");

        //TODO
        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://1d66-2803-1800-421c-e739-3db2-42-a406-e5da.ngrok-free.app/payment-success")
                .failure("https://1d66-2803-1800-421c-e739-3db2-42-a406-e5da.ngrok-free.app/payment-failure")
                .pending("https://1d66-2803-1800-421c-e739-3db2-42-a406-e5da.ngrok-free.app/payment-pending")
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsGateway)
                //TODO agregar id orden
                .metadata(Map.of("id_orden", saveOrder.getId()))
                //TODO Agregar url de Ngrok (Se actualiza constantemente) la ruta debe incluir la direccion al controlador de las notificaciones 
                .notificationUrl("https://1d66-2803-1800-421c-e739-3db2-42-a406-e5da.ngrok-free.app/mercadopago/notification")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        saveOrder.setGatewayCode(preference.getId());
        orderRepo.save(saveOrder);

        PaymentResponseDTO paymentResponse = new PaymentResponseDTO(
                preference.getInitPoint(),
                idOrden
        );

        return paymentResponse;
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
                com.mercadopago.resources.payment.Payment payment = client.get(Long.parseLong(idPago));


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
        orderPayment.setDate(payment.getDateCreated().toLocalDateTime());
        orderPayment.setStatus(payment.getStatus());
        orderPayment.setStatusDetail(payment.getStatusDetail());
        orderPayment.setPaymentType(payment.getPaymentTypeId());
        orderPayment.setCurrency(payment.getCurrencyId());
        orderPayment.setAuthorizationCode(payment.getAuthorizationCode());
        orderPayment.setTransactionValue(payment.getTransactionAmount().floatValue());
        return orderPayment;
    }


    @Override
    public String sendPurchaseSummary(String email, Order order) throws Exception {
        Account account = accountService.getAccountEmail(email);

        // Generar código QR en


        //TODO Send this code to the user (Account) email
        String subject = "Ticket Purchase Summary";
        String body = "Hello " + account.getUser().getName() + "!\n\n"
                + "Thank you for your purchase. Below is a summary of your order:\n\n"
                + "Order Number: " + order.getId() + "\n"
                + "Purchase Date: " + order.getDate() + "\n";
        if(order.getPayment()!=null) {
            body +="Payment Method: " + order.getPayment().getPaymentType().toString().toLowerCase() + "\n"
                    + "Payment Status: " + order.getPayment().getStatus() + "\n\n";
        }

        body += "Event Details:\n";
        for (OrderDetail item : order.getItems()) {

            Event event = eventService.getEvent(item.getEventId().toString());
            body += "---------------------------------\n"
                    + "Event: " + event.getName() + "\n"
                    + "Event Date: " + event.getDate() + "\n"
                    + "Location: " + item.getLocationName() + "\n"
                    + "Number of Tickets: " + item.getQuantity() + "\n"
                    + "Total: " + (item.getPrice()) + "\n"
                    + "---------------------------------\n";
        }

        body += "Total Paid: " + order.getTotal() + "\n\n";

        if (order.getCouponId() != null) {
            Coupon coupon = couponService.getCouponById(order.getCouponId().toString());
            body += "Coupon used: " + coupon.getCode() + "\n"
                    + "Discount applied: " + coupon.getDiscount() * 100 + "%" + "\n\n";
        }
        //TODO Add QR with id Order
        body += """
                To access your tickets, scan the following QR code:\s
                
                """;
        body += "<img src='cid:qrCodeImage'/>\n\n"; // Usar CID para incrustar la imagen

        body += """
                We hope you enjoy the event!
                Sincerely,
                Unieventos Team""";


        return "The summary of your purchase has been sent to your email";

    }








}
