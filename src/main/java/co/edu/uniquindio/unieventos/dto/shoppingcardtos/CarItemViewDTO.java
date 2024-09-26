package co.edu.uniquindio.unieventos.dto.shoppingcardtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;

public record CarItemViewDTO(
        String eventName,
        String location,
        EventType eventType,
        float price,
        int quantity,
        float total
) {
}
