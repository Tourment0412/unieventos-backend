package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.orderdtos.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderFilterDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderInfoDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderItemDTO;

import java.util.List;

public interface OrderService {
    String createOrder(CreateOrderDTO createOrderDTO) throws Exception;
    String deleteOrder(String orderId) throws Exception;
    OrderInfoDTO getInfoOrder(String orderId) throws Exception;



    List<OrderItemDTO> listOrdersAdmin();
    List<OrderItemDTO> listOrdersCient(String cientId) throws Exception;

    List<OrderItemDTO> filterOrders(OrderFilterDTO filterOrderDTO);
    void makePayment(String orderId);

}
