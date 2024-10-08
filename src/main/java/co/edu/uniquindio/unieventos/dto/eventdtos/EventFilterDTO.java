package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record EventFilterDTO(
        @Length(max = 100, message = "Event name must not exceed 100 characters")
        String name,

        @NotNull(message = "Event type is required")
        EventType eventType,

        @Length(max = 50, message = "Event city must not exceed 50 characters")
        String city,

        @Min(value = 0, message = "Page number must be zero or a positive number")
        int page
) {
}
