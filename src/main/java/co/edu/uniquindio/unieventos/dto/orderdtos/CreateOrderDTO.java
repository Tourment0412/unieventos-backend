package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;

import java.util.List;

public record CreateOrderDTO(

        boolean isForFriend,
        String friendEmail,
        String clientId,
        String couponId
) {



}
