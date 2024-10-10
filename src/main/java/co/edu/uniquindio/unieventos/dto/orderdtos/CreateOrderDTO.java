package co.edu.uniquindio.unieventos.dto.orderdtos;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderDTO(
        @NotBlank(message = "Client ID cannot be empty")
        String clientId,

        String couponCode
) {



}
