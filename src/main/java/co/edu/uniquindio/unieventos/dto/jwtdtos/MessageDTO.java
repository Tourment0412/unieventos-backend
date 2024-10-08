package co.edu.uniquindio.unieventos.dto.jwtdtos;

import jakarta.validation.constraints.NotNull;

public record MessageDTO<T>(
        boolean error,

        T reply
) {
}
