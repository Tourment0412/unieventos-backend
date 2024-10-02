package co.edu.uniquindio.unieventos.dto.eventdtos;

import java.time.LocalDateTime;

public record EventItemDTO(
        String id,
        String name,
        LocalDateTime date,
        String address,
        String coverImage
) {
}
