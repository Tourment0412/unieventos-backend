package co.edu.uniquindio.unieventos.dto.emaildtos;

public record EmailDTO(
        String subject,
        String body,
        String receiver
) {
}
