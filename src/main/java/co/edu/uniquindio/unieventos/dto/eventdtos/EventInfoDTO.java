package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;


import java.time.LocalDateTime;
import java.util.List;

public record EventInfoDTO(

        String name,
        String address,
        String coverImage,
        String localitiesImage,
        LocalDateTime date,
        String description,
        EventType type,
        List<Location> locations
) {
}