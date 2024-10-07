package co.edu.uniquindio.unieventos.dto.accountdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDTO(
        @NotBlank(message = "Email can't be empty")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "The password is mandatory")
        @Size(min = 7, max = 20, message = "The password must be between 7 and 20 characters long")
        String password
) {
}
