package co.edu.uniquindio.unieventos.services.implementations;


import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.model.documents.*;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.model.vo.*;
import co.edu.uniquindio.unieventos.repositories.AccountRepo;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.repositories.OrderRepo;
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

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImp implements OrderService {
    private final OrderRepo orderRepo;
    private final CouponService couponService;
    private final EventService eventService;
    private final ShoppingCarService shoppingCarService;
    private final AccountRepo accountRepo;
    private final EmailService emailService;
    private final EventRepo eventRepo;
    private final CouponRepo couponRepo;


    public OrderServiceImp(OrderRepo orderRepo, CouponService couponService, EventService eventService, ShoppingCarService shoppingCarService, AccountRepo accountRepo, EmailService emailService, EventRepo eventRepo, CouponRepo couponRepo) {
        this.orderRepo = orderRepo;
        this.couponService = couponService;
        this.eventService = eventService;
        this.shoppingCarService = shoppingCarService;
        this.accountRepo = accountRepo;
        this.emailService = emailService;
        this.eventRepo = eventRepo;
        this.couponRepo = couponRepo;
    }

    @Override
    public String createOrder(CreateOrderDTO createOrderDTO) throws Exception {
        Order order = new Order();
        ShoppingCar shoppingCar = shoppingCarService.getShoppingCar(createOrderDTO.clientId());
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

        //TODO code for gateway
        //TODO Payment
        order.setItems(items);
        order.setDate(LocalDateTime.now());
        order.setTotal(calculateTotal(items, createOrderDTO.couponId()));
        order.setClientId(new ObjectId(createOrderDTO.clientId()));
        if(!(createOrderDTO.couponId()==null)){
            order.setCouponId(new ObjectId(createOrderDTO.couponId()));
        }

        order.setGift(false);
        //TODO validar si está registrada
        if(createOrderDTO.isForFriend()){
            if(accountRepo.findAccountByEmail(createOrderDTO.friendEmail()).isEmpty()){
               throw new Exception("Your friend is not registered ");
            }
            order.setGift(true);
            order.setFriendMail(createOrderDTO.friendEmail());

        }
        Account account = accountRepo.findAccountById(order.getClientId().toString()).get();
        Order createOrder = orderRepo.save(order);
        sendPurchaseSummary(account.getEmail(), order);
        return createOrder.getId();
    }

    private float calculateTotal(List<OrderDetail> items, String couponId) throws Exception {
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
        }
        return total - (total * coupon.getDiscount());
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
        Order order = getOrder(orderId);

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
    public List<OrderItemDTO> listOrdersClient(String idClient) throws Exception {
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
    public List<OrderItemDTO> filterOrders(OrderFilterDTO filterOrderDTO) {
        return List.of();
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
        Account account = getAccountEmail(email);

        // Generar código QR
        String orderId = order.getId();
        String tempDir = System.getProperty("java.io.tmpdir"); // Directorio temporal
        String qrFilePath = tempDir + "/" + UUID.randomUUID() + ".png"; // Generar nombre único para evitar conflictos

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
            Event event = eventRepo.findById(item.getEventId().toString()).get();
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
            Coupon coupon = couponRepo.findById(order.getCouponId().toString()).get();
            body += "Coupon used: " + coupon.getCode() + "\n"
                    + "Discount applied: " + coupon.getDiscount() * 100 + "%" + "\n\n";
        }
        //TODO Add QR with id Order
        body += "To access your tickets, scan the following QR code: " + "\n\n";
        body += "<img src='cid:qrCodeImage'/>\n\n"; // Usar CID para incrustar la imagen

        body += "We hope you enjoy the event!\n"
                + "Sincerely,\n"
                + "Unieventos Team";


        // Enviar el correo
        //emailService.sendEmailWithInlineImage(new EmailDTO(subject, body, account.getEmail(), qrFilePath));
        // Eliminar archivo QR después de enviar el correo
        new File(qrFilePath).delete();
        return "The summary of your purchase has been sent to your email";

    }


    private Account getAccountEmail(String email) throws Exception {
        Optional<Account> accountOptional = accountRepo.findAccountByEmail(email);
        if (accountOptional.isEmpty()) {
            throw new Exception("This email is not registered");
        }
        return accountOptional.get();
    }

}
