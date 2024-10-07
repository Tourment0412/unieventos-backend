package co.edu.uniquindio.unieventos.dto.orderdtos;

import jakarta.validation.constraints.NotBlank;

public record PaymentResponseDTO(

        @NotBlank(message = "Payment URL cannot be empty")
        String paymentUrl,

        @NotBlank(message = "Order ID cannot be empty")
        String idOrder
) {
}
