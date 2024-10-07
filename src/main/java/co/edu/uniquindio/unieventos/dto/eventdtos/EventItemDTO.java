package co.edu.uniquindio.unieventos.dto.eventdtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record EventItemDTO(
        @NotNull(message = "ID cannot be null")
        String id,

        @NotBlank(message = "Event name cannot be empty")
        @Length(max = 100, message = "Event name must not exceed 100 characters")
        String name,

        @Future(message = "Event date must be in the future")
        @NotNull(message = "Event date cannot be null")
        LocalDateTime date,

        @NotBlank(message = "Event address cannot be empty")
        @Length(max = 200, message = "Event address must not exceed 200 characters")
        String address,

        @NotBlank(message = "Cover image route cannot be empty")
        String coverImage
) {
}
