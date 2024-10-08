package co.edu.uniquindio.unieventos.dto.jwtdtos;

import jakarta.validation.constraints.NotBlank;

public record ValidationDTO(
        String field,
        String message
) {
}
