package co.edu.uniquindio.unieventos.dto;

public record ChangePasswordDTO(
        String email,
        String verificationCode,
        String newPassword

) {
}
