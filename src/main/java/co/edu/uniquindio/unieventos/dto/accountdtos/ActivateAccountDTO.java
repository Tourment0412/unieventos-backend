package co.edu.uniquindio.unieventos.dto.accountdtos;

public record ActivateAccountDTO(
        String email,
        String registrationValidationCode
) {
}
