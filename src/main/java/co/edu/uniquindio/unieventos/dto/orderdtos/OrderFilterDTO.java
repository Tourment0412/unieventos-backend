package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record OrderFilterDTO(
        @NotBlank(message = "Event ID cannot be empty")
        String eventId,

        @NotNull(message = "Order date cannot be null")
        LocalDateTime orderDate

) {

}
