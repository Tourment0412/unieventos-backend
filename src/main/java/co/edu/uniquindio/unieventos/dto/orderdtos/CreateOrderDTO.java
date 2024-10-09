package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateOrderDTO(
        @NotBlank(message = "Client ID cannot be empty")
        String clientId,

        String counponCode
) {



}
