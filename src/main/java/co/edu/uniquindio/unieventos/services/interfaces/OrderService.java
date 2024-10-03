package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.orderdtos.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderFilterDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderInfoDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderItemDTO;
import com.mercadopago.resources.preference.Preference;

import java.util.List;
import java.util.Map;

public interface OrderService {
    String createOrder(CreateOrderDTO createOrderDTO) throws Exception;
    String deleteOrder(String orderId) throws Exception;
    OrderInfoDTO getInfoOrder(String orderId) throws Exception;



    List<OrderItemDTO> listOrdersAdmin();
    List<OrderItemDTO> listOrdersClient(String clientId) throws Exception;

    List<OrderItemDTO> filterOrders(OrderFilterDTO filterOrderDTO);

    
    //Methods for payment gateway
    Preference makePayment(String idOrden) throws Exception;
    void receiveNotificationFromMercadoPago(Map<String, Object> request);

}
