package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record EventFilterDTO(
        String name,
        EventType eventType,
        String city,

        @Min(value = 0, message = "Page number must be zero or a positive number")
        int page
) {
}
