package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


import java.time.LocalDateTime;
import java.util.List;

public record EventInfoDTO(
        String id,
        String name,
        String address,
        String city,
        String coverImage,
        String localitiesImage,
        LocalDateTime date,
        String description,
        EventType type,
        List<Location> locations
) {
}
