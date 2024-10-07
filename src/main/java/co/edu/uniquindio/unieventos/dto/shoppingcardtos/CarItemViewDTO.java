package co.edu.uniquindio.unieventos.dto.shoppingcardtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CarItemViewDTO(
        @NotBlank(message = "Event name cannot be empty")
        String eventName,

        @NotBlank(message = "Location cannot be empty")
        String location,

        @NotNull(message = "Event type is required")
        EventType eventType,

        @Min(value = 0, message = "Price must be at least 0")
        float price,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity,

        @Min(value = 0, message = "Total must be at least 0")
        float total
) {
}
