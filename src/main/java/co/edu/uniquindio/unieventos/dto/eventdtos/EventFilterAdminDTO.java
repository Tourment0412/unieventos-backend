package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;

public record EventFilterAdminDTO(
        String name,
        EventType eventType,
        String city

) {
}
