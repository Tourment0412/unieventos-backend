package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.orderdtos.*;
import co.edu.uniquindio.unieventos.exceptions.EmptyShoppingCarException;
import co.edu.uniquindio.unieventos.exceptions.OperationNotAllowedException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
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
    String createOrder(CreateOrderDTO createOrderDTO) throws EmptyShoppingCarException, ResourceNotFoundException, Exception;

    /**
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    String deleteOrder(String orderId) throws ResourceNotFoundException, OperationNotAllowedException;

    /**
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    OrderItemDTO getInfoOrder(String orderId) throws ResourceNotFoundException;

    /**
     *
     * @param clientId
     * @return
     * @throws Exception
     */
    List<OrderItemDTO> listOrdersClient(String clientId) ;

    /**
     *
     * @param email
     * @param order
     * @return
     * @throws Exception
     */
    String sendPurchaseSummary(String email, Order order) throws ResourceNotFoundException, Exception;

    /**
     *
     * @param s
     * @return
     * @throws Exception
     */
    Order getOrder(String s) throws ResourceNotFoundException;

    /**
     *
     * @param giftDTO
     * @return
     * @throws Exception
     */
    String sendGift(GiftDTO giftDTO) throws ResourceNotFoundException, OperationNotAllowedException, Exception;

    /**
     *
     * @param idOrder
     * @return
     * @throws Exception
     */
    //Methods for payment gateway
    PaymentResponseDTO makePayment(String idOrder) throws  ResourceNotFoundException, OperationNotAllowedException, Exception;

    /**
     *
     * @param request
     */
    void receiveNotificationFromMercadoPago(Map<String, Object> request);

    boolean hasClientUsedCoupon(String clientId, String couponId);

}
