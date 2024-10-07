package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record OrderItemDTO(
        @NotBlank(message = "Client ID cannot be empty")
        String clientId,

        @NotNull(message = "Order date cannot be null")
        LocalDateTime orderDate,

        @NotEmpty(message = "Order items cannot be empty")
        List<OrderDetail> items,

        @NotBlank(message = "Payment type cannot be empty")
        String paymentType,

        @NotBlank(message = "Order status cannot be empty")
        String status,

        @FutureOrPresent(message = "Payment date must be in the present or future")
        LocalDateTime paymentDate,

        @DecimalMin(value = "0.0", inclusive = false, message = "Transaction value must be greater than 0")
        float transactionValue,//?

        @NotBlank(message = "Order ID cannot be empty")
        String id,

        @DecimalMin(value = "0.0", inclusive = false, message = "Total must be greater than 0")
        float total,

        String couponCode

) {
}
