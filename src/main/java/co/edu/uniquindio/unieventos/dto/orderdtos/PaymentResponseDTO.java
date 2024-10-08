package co.edu.uniquindio.unieventos.dto.orderdtos;

import jakarta.validation.constraints.NotBlank;

public record PaymentResponseDTO(
        String paymentUrl,
        String idOrder
) {
}
