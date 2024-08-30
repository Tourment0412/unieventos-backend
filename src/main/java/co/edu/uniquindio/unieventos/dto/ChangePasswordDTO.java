package co.edu.uniquindio.unieventos.dto;

public record ChangePasswordDTO(
        String verificationCode,
        String newPassword

) {
}
