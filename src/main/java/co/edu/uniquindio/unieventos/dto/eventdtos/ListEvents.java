package co.edu.uniquindio.unieventos.dto.eventdtos;

import java.awt.event.ItemEvent;
import java.util.List;

public record ListEvents(
        int totalPages,
        List<EventItemDTO> events
) {
}
