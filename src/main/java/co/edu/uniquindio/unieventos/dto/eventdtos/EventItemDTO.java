package co.edu.uniquindio.unieventos.dto.eventdtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record EventItemDTO(
        String id,
        String name,
        LocalDateTime date,
        String address,
        String city,
        String coverImage
) {
}
