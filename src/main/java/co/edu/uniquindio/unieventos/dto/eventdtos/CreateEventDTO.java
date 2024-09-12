package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;

import java.time.LocalDateTime;
import java.util.List;

public record CreateEventDTO(
        String name,
        String address,
        String city,
        String coverImage, //This is going to be a route
        String localitiesImage,//This is going to be a route
        LocalDateTime date,
        String description,
        EventType type,
        List<Location> locations

) {
}
