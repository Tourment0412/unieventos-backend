package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.model.documents.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     *
     * @param createOrderDTO
     * @return
     * @throws Exception
     */
    String createOrder(CreateOrderDTO createOrderDTO) throws Exception;

    /**
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    String deleteOrder(String orderId) throws Exception;

    /**
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    OrderItemDTO getInfoOrder(String orderId) throws Exception;

    /**
     *
     * @param clientId
     * @return
     * @throws Exception
     */
    List<OrderItemDTO> listOrdersClient(String clientId) throws Exception;

    /**
     *
     * @param email
     * @param order
     * @return
     * @throws Exception
     */
    String sendPurchaseSummary(String email, Order order) throws Exception;

    /**
     *
     * @param s
     * @return
     * @throws Exception
     */
    Order getOrder(String s) throws Exception;

    /**
     *
     * @param giftDTO
     * @return
     * @throws Exception
     */
    String sendGift(GiftDTO giftDTO) throws Exception;

    /**
     *
     * @param idOrder
     * @return
     * @throws Exception
     */
    //Methods for payment gateway
    PaymentResponseDTO makePayment(String idOrder) throws Exception;

    /**
     *
     * @param request
     */
    void receiveNotificationFromMercadoPago(Map<String, Object> request);

}
