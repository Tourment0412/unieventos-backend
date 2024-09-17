package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;

public record EventFilterDTO(
        String name,
        EventType eventType,
        String city
) {
}
