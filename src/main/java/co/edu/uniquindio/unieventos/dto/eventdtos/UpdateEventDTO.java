package co.edu.uniquindio.unieventos.dto.eventdtos;

import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateEventDTO(
        @NotNull(message = "ID cannot be null")
        String id,

        @NotBlank(message = "Event name cannot be empty")
        @Length(max = 100, message = "Event name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Event address cannot be empty")
        @Length(max = 200, message = "Event address must not exceed 200 characters")
        String address,

        @NotBlank(message = "Event city cannot be empty")
        @Length(max = 50, message = "Event city must not exceed 50 characters")
        String city,

        @NotBlank(message = "Cover image route cannot be empty")
        String coverImage, //This is going to be a route

        @NotBlank(message = "Localities image route cannot be empty")
        String localitiesImage,//This is going to be a route

        @Future(message = "Event date must be in the future")
        @NotNull(message = "Event date cannot be null")
        LocalDateTime date,

        @NotBlank(message = "Event description cannot be empty")
        @Length(max = 500, message = "Event description must not exceed 500 characters")
        String description,

        @NotNull(message = "Event type is required")
        EventType type,

        @NotNull(message = "Event status is required")
        EventStatus status,

        @NotEmpty(message = "Event must have at least one location")
        List<Location> locations
) {
}
