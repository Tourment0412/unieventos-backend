package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.orderdtos.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderFilterDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderInfoDTO;
import co.edu.uniquindio.unieventos.dto.orderdtos.OrderItemDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;
import co.edu.uniquindio.unieventos.repositories.CouponRepo;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.repositories.OrderRepo;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class OrderServiceImp implements OrderService {
    private final OrderRepo orderRepo;
    private final CouponRepo couponRepo;
    private final EventRepo eventRepo;

    public OrderServiceImp(OrderRepo orderRepo, CouponRepo couponRepo, EventRepo eventRepo) {
        this.orderRepo = orderRepo;
        this.couponRepo = couponRepo;
        this.eventRepo = eventRepo;
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
    public void makePayment(String orderId) {

    }
}
