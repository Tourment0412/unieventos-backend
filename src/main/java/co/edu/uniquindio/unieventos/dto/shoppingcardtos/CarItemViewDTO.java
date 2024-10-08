package co.edu.uniquindio.unieventos.dto.shoppingcardtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CarItemViewDTO(
        String eventName,
        String location,
        EventType eventType,
        float price,
        int quantity,
        float total
) {
}
