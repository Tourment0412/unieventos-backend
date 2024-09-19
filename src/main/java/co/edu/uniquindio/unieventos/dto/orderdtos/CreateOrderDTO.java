package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;

import java.util.List;

public record CreateOrderDTO(


        String clientId,
        List<OrderDetail> items,
        String couponId
) {



}
