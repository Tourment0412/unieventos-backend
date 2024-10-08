package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record OrderItemDTO(
        String clientId,
        LocalDateTime orderDate,
        List<OrderDetailDTO> items,
        String paymentType,
        String status,
        LocalDateTime paymentDate,
        float transactionValue,//?
        String id,
        float total,
        String couponId

) {
}
