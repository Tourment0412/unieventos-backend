package co.edu.uniquindio.unieventos.dto.jwtdtos;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(
        String token
) {
}
