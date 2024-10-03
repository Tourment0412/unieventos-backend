package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;


import java.time.LocalDateTime;
import java.util.List;

public record EventInfoDTO(
        //TODO (ask) Should I return the information with the id too? I think yes because here
        // I'm going to show the user the info and gonna be the option to add to the shopping car
        String id,

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
