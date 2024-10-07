package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    String createOrder(CreateOrderDTO createOrderDTO) throws Exception;
    String deleteOrder(String orderId) throws Exception;
    OrderInfoDTO getInfoOrder(String orderId) throws Exception;
    List<OrderItemDTO> listOrdersClient(String clientId) throws Exception;
    String sendPurchaseSummary(String email, Order order) throws Exception;


    //Methods for payment gateway
    PaymentResponseDTO makePayment(String idOrder) throws Exception;
    void receiveNotificationFromMercadoPago(Map<String, Object> request);

}
