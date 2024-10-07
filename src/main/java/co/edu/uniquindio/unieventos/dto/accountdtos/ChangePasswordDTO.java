package co.edu.uniquindio.unieventos.dto.accountdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
        @NotBlank(message = "Email can't be empty")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Verification code can't be empty")
        @Size(min = 6, max = 6, message = "The verification code must be 6 characters in size")
        String verificationCode,

        @NotBlank(message = "The password is mandatory")
        @Size(min = 7, max = 20, message = "The new password must be between 7 and 20 characters long")
        String newPassword
){}
