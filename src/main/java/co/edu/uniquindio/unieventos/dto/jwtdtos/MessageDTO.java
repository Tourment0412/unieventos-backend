package co.edu.uniquindio.unieventos.dto.jwtdtos;

public record MessageDTO<T>(
        boolean error,
        T reply
) {
}
