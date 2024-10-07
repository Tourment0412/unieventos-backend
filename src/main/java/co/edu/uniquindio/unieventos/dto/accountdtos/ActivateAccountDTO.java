package co.edu.uniquindio.unieventos.dto.accountdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivateAccountDTO(
        @NotBlank(message = "Email can't be empty")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "Registration validation code must not be blank.")
        @Size(min = 6, max = 6, message = "The Registration validation code must be 6 characters in size")
        String registrationValidationCode
) {
}
