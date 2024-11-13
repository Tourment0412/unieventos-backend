package co.edu.uniquindio.unieventos.services.implementations;


import co.edu.uniquindio.unieventos.dto.emaildtos.EmailDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.exceptions.EmptyShoppingCarException;
import co.edu.uniquindio.unieventos.exceptions.InsufficientCapacityException;
import co.edu.uniquindio.unieventos.exceptions.OperationNotAllowedException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
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
import com.mercadopago.resources.payment.PaymentStatus;
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
    public String createOrder(CreateOrderDTO createOrderDTO)
            throws EmptyShoppingCarException, ResourceNotFoundException, OperationNotAllowedException, Exception {

        ShoppingCar shoppingCar = shoppingCarService.getShoppingCar(createOrderDTO.clientId());
        List<OrderDetail> items = getOrderDetails(shoppingCar);

        Order order = new Order();
        order.setItems(items);
        order.setDate(LocalDateTime.now());
        order.setClientId(new ObjectId(createOrderDTO.clientId()));
        order.setGift(false);

        if (createOrderDTO.couponCode() != null && !createOrderDTO.couponCode().isEmpty()) {

            Coupon coupon = couponService.getCouponByCode(createOrderDTO.couponCode());

            if (coupon == null) {
                throw new ResourceNotFoundException("El cupon proporcionado no existe");
            }

            // Validar que el cupón tiene un id válido antes de usarlo
            if (coupon != null && coupon.getId() == null) {
                throw new ResourceNotFoundException("El cupón no tiene un id válido para crear la orden");
            }

            validateCouponUsage(createOrderDTO, order);

            float totalWithDiscount = calculateTotal(items, coupon.getId(), createOrderDTO.clientId());
            order.setTotal(totalWithDiscount);

            if (coupon.getType() == CouponType.UNIQUE) {
                couponService.deleteCoupon(coupon.getId());
            }
        } else {
            order.setTotal(calculateTotal(items, null, createOrderDTO.clientId()));
        }

        Account account = accountService.getAccount(createOrderDTO.clientId());
        Order createOrder = orderRepo.save(order);

        sendPurchaseSummary(account.getEmail(), order);

        shoppingCarService.deleteShoppingCar(createOrderDTO.clientId());

        return createOrder.getId();
    }



    @Override
    public boolean hasClientUsedCoupon(String clientId, String couponId) throws ResourceNotFoundException {
        if (couponId == null) {
            throw new ResourceNotFoundException("El cupon no fue encontrado");
        }
        return orderRepo.existsByClientIdAndCouponId(new ObjectId(clientId), new ObjectId(couponId));
    }

    private void validateCouponUsage(CreateOrderDTO createOrderDTO, Order order)
            throws ResourceNotFoundException, OperationNotAllowedException {
        
        Coupon couponOrder = couponService.getCouponByCode(createOrderDTO.couponCode());

        if (couponOrder == null) {
            throw new ResourceNotFoundException("Coupon not found with code: " + createOrderDTO.couponCode());
        }

        if (couponOrder.getId() == null) {
            throw new ResourceNotFoundException("El cupón no tiene un id válido.");
        }

        List<Order> ordersClient = getOrdersByIdClient(createOrderDTO.clientId());
        for (Order orderClient : ordersClient) {

            if (orderClient.getCouponId() != null) {
                Coupon couponClient = couponService.getCouponById(orderClient.getCouponId().toString());

                if (couponClient != null && couponClient.getCode().equals(createOrderDTO.couponCode())) {
                    throw new OperationNotAllowedException("You can't use a coupon you previously used");
                }
            }
        }

        if (couponOrder.getId() != null) {
            order.setTotal(calculateTotal(order.getItems(), couponOrder.getId(), createOrderDTO.clientId()));
        }

        order.setCouponId(new ObjectId(couponOrder.getId()));
    }



    private @NotNull List<OrderDetail> getOrderDetails(ShoppingCar shoppingCar) {
        List<OrderDetail> items = new ArrayList<>();
        List<CarDetail> details = shoppingCar.getItems();
        details.forEach(carDetail -> {
            try {

                Event event = eventService.getEvent(String.valueOf(carDetail.getIdEvent()));
                Location location = event.findLocationByName(carDetail.getLocationName());
                if (!location.isCapacityAvaible(carDetail.getAmount())) {
                    throw new InsufficientCapacityException("Max capacity exceeded");
                } else if (event.getDate().minusDays(2).isBefore(LocalDateTime.now()) || event.getDate().minusDays(2).equals(LocalDateTime.now())) { //Se tendria que añadir un or para que la fecha sea igual
                    throw new OperationNotAllowedException("Date not valid");
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


    private float calculateTotal(List<OrderDetail> items, String couponId, String idClient)
            throws ResourceNotFoundException, OperationNotAllowedException {
        if (couponId != null && !couponId.isEmpty()) {

            Coupon coupon = couponService.getCouponById(couponId);

            if (coupon == null) {
                throw new ResourceNotFoundException("Coupon not found with id: " + couponId);
            }

            if (coupon.getStatus() == CouponStatus.NOT_AVAILABLE) {
                throw new OperationNotAllowedException("Coupon is not available");
            }

            float total = 0;
            for (OrderDetail orderDetail : items) {
                total += orderDetail.getPrice();
            }

            if (coupon.getType() == CouponType.MULTIPLE) {
                List<Order> ordersClient = getOrdersByIdClient(idClient);
                for (Order order : ordersClient) {
                    // Verificar que el CouponId de la orden no sea nulo antes de compararlo
                    if (order.getCouponId() != null && couponId.equals(order.getCouponId().toString())) {
                        throw new OperationNotAllowedException("Coupon is already in use by this client");
                    }
                }
            }

            return total * (1 - coupon.getDiscount());
        } else {
            float total = 0;
            for (OrderDetail orderDetail : items) {
                total += orderDetail.getPrice();
            }
            return total;
        }
    }


    private List<Order> getOrdersByIdClient(String idClient) {
        return orderRepo.findOrdersByClientId(new ObjectId(idClient));
    }

    @Override
    public Order getOrder(String s) throws ResourceNotFoundException {
        Optional<Order> orderOptional = orderRepo.findById(s);
        if (orderOptional.isEmpty()) {
            throw new ResourceNotFoundException("The Order with the id: " + s + " does not exist");
        }
        return orderOptional.get();
    }

    @Override
    public String deleteOrder(String orderId) throws ResourceNotFoundException, OperationNotAllowedException {
        Order orderToDelete = getOrder(orderId);
        Payment payment = orderToDelete.getPayment();
        if(payment==null || (!(payment.getStatus().equals(PaymentStatus.APPROVED) && payment.getStatusDetail().equalsIgnoreCase("accredited"))) ){
            orderRepo.delete(orderToDelete);
            return "The order was deleted";
        }else{
            throw new OperationNotAllowedException("Unable to delete an order that has already been paid");
        }
    }

    @Override
    public OrderItemDTO getInfoOrder(String orderId) throws ResourceNotFoundException {
        Order order = getOrder(orderId); // Método que obtiene la orden

        return mapToOrderItemDTO(order);
    }

    private OrderItemDTO mapToOrderItemDTO(Order order) {
        return new OrderItemDTO(
                order.getClientId() != null ? order.getClientId().toString() : null,
                order.getDate(),
                mapOrderDetails(order.getItems()),
                order.getPayment() != null ? order.getPayment().getPaymentType() : null,
                order.getPayment() != null ? order.getPayment().getStatus() : null,
                order.getPayment() != null ? order.getPayment().getDate() : null,
                order.getPayment() != null ? order.getPayment().getTransactionValue() : 0f,
                order.getId(),
                order.getTotal(),
                order.getCouponId() != null ? order.getCouponId().toString() : null);
    }


    @Override
    public List<OrderItemDTO> listOrdersClient(String idClient) {
        ObjectId clientId = new ObjectId(idClient);

        List<Order> orders = orderRepo.findOrdersByClientId(clientId);

        return getOrderItemDTOS(orders);
    }

    @NotNull
    private List<OrderItemDTO> getOrderItemDTOS(List<Order> orders) {
        return orders.stream().map(this::mapToOrderItemDTO
        ).collect(Collectors.toList());
    }

    private List<OrderDetailDTO> mapOrderDetails(List<OrderDetail> items) {
        return items.stream().map(e -> new OrderDetailDTO(
                e.getEventId().toString(),
                e.getPrice(),
                e.getLocationName(),
                e.getQuantity()


        )).collect(Collectors.toList());
    }

    @Override
    public PaymentResponseDTO makePayment(String idOrden) throws ResourceNotFoundException, OperationNotAllowedException, Exception {
        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Order saveOrder = getOrder(idOrden);
        List<PreferenceItemRequest> itemsGateway = new ArrayList<>();

        // Comprobar si hay un cupón de descuento en la orden
        Coupon coupon = null;
        if (saveOrder.getCouponId() != null) {
            coupon = couponService.getCouponById(saveOrder.getCouponId().toString());
        }
        List<Order> ordersClient = getOrdersByIdClient(saveOrder.getClientId().toString());

        // Recorrer los items de la orden y crea los ítems de la pasarela
        for (OrderDetail item : saveOrder.getItems()) {
            // Obtener el evento y la localidad del ítem
            Event event = eventService.getEvent(item.getEventId().toString());
            Location location = event.findLocationByName(item.getLocationName());

            float unitPrice = (coupon != null) ?
                    Math.max(0, location.getPrice() - (location.getPrice() * coupon.getDiscount())) :
                    location.getPrice();


                // Crear el item de la pasarela
                PreferenceItemRequest itemRequest =
                        PreferenceItemRequest.builder()
                                .id(event.getId())
                                .title(event.getName())
                                .pictureUrl(event.getCoverImage())
                                .categoryId(event.getType().name())
                                .quantity(item.getQuantity())
                                .currencyId("COP")
                                .unitPrice(BigDecimal.valueOf(unitPrice))
                                .build();
                itemsGateway.add(itemRequest);


        }

        //TODO Configurar las credenciales de MercadoPag. Crear cuenta de mercado pago
        MercadoPagoConfig.setAccessToken("APP_USR-8178646482281064-100513-248819fc76ea7f7577f902e927eaefb7-2014458486");

        //TODO
        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://smooth-unicorn-trusting.ngrok-free.app/?status=success")
                .failure("https://smooth-unicorn-trusting.ngrok-free.app/?status=failure")
                .pending("https://smooth-unicorn-trusting.ngrok-free.app/?status=pending")
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsGateway)
                //TODO agregar id orden
                .metadata(Map.of("id_orden", saveOrder.getId()))
                //TODO Agregar url de Ngrok (Se actualiza constantemente) la ruta debe incluir la direccion al controlador de las notificaciones 
                .notificationUrl("https://smooth-unicorn-trusting.ngrok-free.app/api/public/order/receive-notification")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        saveOrder.setGatewayCode(preference.getId());
        orderRepo.save(saveOrder);



        return new PaymentResponseDTO(
                preference.getInitPoint(),
                idOrden
        );
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

                // Se obtiene la orden guardada en la base de datos y se le asigna el pago, ademas de aumentar la cantidad de entradas vendidas
                Order order = getOrder(idOrden);
                Payment orderPayment = createPayment(payment);

                order.setPayment(orderPayment);
                orderRepo.save(order);
                Account account = accountService.getAccount(order.getClientId().toString());

                List<Order> ordersClient = getOrdersByIdClient(account.getId());
                if (order.getPayment().getStatus().equalsIgnoreCase("APPROVED") && order.getPayment().getStatusDetail().equalsIgnoreCase("accredited")) {
                    for (OrderDetail orderDetail : order.getItems()){
                        eventService.reduceNumberLocations(orderDetail.getQuantity(), orderDetail.getLocationName(), orderDetail.getEventId().toString());
                    }
                    sendPurchaseSummary(account.getEmail(), order);
                    if(ordersClient.size()==1){
                        //TODO crear cupon con codigo FIRST1
                        sendCouponFirstPurchase(account.getEmail());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendCouponFirstPurchase(String email) throws Exception {
        String subject = "Welcome to Unieventos - Enjoy a Gift for Your First Purchase!";
        String body = String.format(
                "Hello,\n\n" +
                        "Thank you for making your first purchase with Unieventos! As a token of our appreciation, we have a special gift for you.\n\n" +
                        "Your exclusive Coupon Code: %s\n\n" +
                        "Use this code at checkout to receive a discount on your next purchase.\n\n" +
                        "We are excited to have you with us, and we hope you enjoy all the exciting events available on our platform.\n\n" +
                        "If you have any questions, feel free to contact our support team.\n\n" +
                        "Best regards,\n" +
                        "The Unieventos Team",
                "FIRST1"
        );
        emailService.sendEmail(new EmailDTO(subject, body, email));
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
    public String sendGift(GiftDTO giftDTO) throws ResourceNotFoundException, OperationNotAllowedException, Exception {
        Order order = getOrder(giftDTO.idOrder());
        if(!(order.getPayment().getStatus().equals(PaymentStatus.APPROVED))){
            throw new OperationNotAllowedException("You have to pay for the order before making a gift");
        }

        if(order.isGift()){
            throw new OperationNotAllowedException("You cannot give tickets more than once");
        }

        Account account = accountService.getAccountEmail(giftDTO.friendEmail());
        order.setGift(true);
        order.setFriendMail(giftDTO.friendEmail());

        String subject = "Your friend bought you tickets";
        StringBuilder body = new StringBuilder();

        body.append("<html><body>");
        body.append("<h1>Hello ").append(account.getUser().getName()).append("!</h1>");
        body.append("<p>Your friend bought you tickets to the following events. Enjoy them!!:</p>");
        List<String> listOfEventIds = new ArrayList<>();
        for (OrderDetail item : order.getItems()) {
            Event event = eventService.getEvent(item.getEventId().toString());
            body.append("<p>---------------------------------<br>")
                    .append("Event: ").append(event.getName()).append("<br>")
                    .append("Event Date: ").append(event.getDate()).append("<br>")
                    .append("Location: ").append(item.getLocationName()).append("<br>")
                    .append("Number of Tickets: ").append(item.getQuantity()).append("<br>")
                    .append("Total: ").append(item.getPrice()).append("<br>")
                    .append("---------------------------------</p>");
            listOfEventIds.add(event.getId().toString());
        }
        String combinedIds = String.join(",", listOfEventIds);
        String qrCodeUrl = "https://quickchart.io/qr?text=" + combinedIds + "&size=300";
        byte[] qrCodeImage = emailService.downloadImage(qrCodeUrl);

        body.append("<p>To access your tickets, scan the following QR code:</p>");
        body.append("<img src='cid:qrCodeImage'/>");
        body.append("<br><p>We hope you enjoy the event!<br>Sincerely,<br>Unieventos Team</p>");
        body.append("</body></html>");

        emailService.sendEmailHtmlWithAttachment(new EmailDTO(subject, body.toString(), giftDTO.friendEmail()), qrCodeImage, "qrCodeImage");
        orderRepo.save(order);

        return "The tickets have been given to your friend";
    }

    @Override
    public String sendPurchaseSummary(String email, Order order) throws ResourceNotFoundException, Exception {
        Account account = accountService.getAccountEmail(email);

        String qrCodeUrl = "https://quickchart.io/qr?text=" + order.getId() + "&size=300";
        byte[] qrCodeImage = emailService.downloadImage(qrCodeUrl);

        String subject = "Summary of your purchase";
        StringBuilder body = new StringBuilder();

        body.append("<html><body>");
        body.append("<h1>Hello ").append(account.getUser().getName()).append("!</h1>");
        body.append("<p>Thank you for your purchase. Below is a summary of your order:</p>");

        body.append("<h3>Order Summary:</h3>");
        body.append("<p>Order Number: ").append(order.getId()).append("<br>") //El correo se está enviando antes de crear la orden por lo q
                .append("Purchase Date: ").append(order.getDate()).append("</p>");

        if (order.getPayment() != null) {
            body.append("<p>Payment Method: ").append(order.getPayment().getPaymentType().toLowerCase()).append("<br>")
                    .append("Payment Status: ").append(order.getPayment().getStatus()).append("</p>");
        }

        body.append("<h3>Event Details:</h3>");
        for (OrderDetail item : order.getItems()) {
            Event event = eventService.getEvent(item.getEventId().toString());
            body.append("<p>---------------------------------<br>")
                    .append("Event: ").append(event.getName()).append("<br>")
                    .append("Event Date: ").append(event.getDate()).append("<br>")
                    .append("Location: ").append(item.getLocationName()).append("<br>")
                    .append("Number of Tickets: ").append(item.getQuantity()).append("<br>")
                    .append("Total: ").append(item.getPrice()).append("<br>")
                    .append("---------------------------------</p>");
        }

        body.append("<p>Total Paid: ").append(order.getTotal()).append("</p>");

        if (order.getCouponId() != null) {
            Coupon coupon = couponService.getCouponById(order.getCouponId().toString());
            body.append("<p>Coupon used: ").append(coupon.getCode()).append("<br>")
                    .append("Discount applied: ").append(coupon.getDiscount() * 100).append("%</p>");
        }

        body.append("<p>To view your order, scan the following QR code:</p>");
        body.append("<img src='cid:qrCodeImage'/>");
        body.append("<br><p>We hope you enjoy the event!<br>Sincerely,<br>Unieventos Team</p>");
        body.append("</body></html>");

        // Enviar el correo con la imagen embebida
        emailService.sendEmailHtmlWithAttachment(new EmailDTO(subject, body.toString(), email), qrCodeImage, "qrCodeImage");

        return "The summary of your purchase has been sent to your email";
    }




}
