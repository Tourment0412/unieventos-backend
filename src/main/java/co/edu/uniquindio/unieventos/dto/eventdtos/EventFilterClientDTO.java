package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;

import java.time.LocalDateTime;

public record EventFilterClientDTO(
        String name,
        EventType eventType,
        String city
) {
}
