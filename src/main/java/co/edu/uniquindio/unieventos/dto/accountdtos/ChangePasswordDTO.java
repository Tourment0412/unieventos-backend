package co.edu.uniquindio.unieventos.dto.accountdtos;

public record ChangePasswordDTO(
        String email,
        String verificationCode,
        String newPassword

) {
}
