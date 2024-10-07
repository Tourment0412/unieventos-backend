package co.edu.uniquindio.unieventos.dto.accountdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record UserObtainedByMailPassDTO(
        @Size(min = 10, max = 15, message = "The phone number must be between 10 and 15 characters long")
        String phoneNumber,

        @Size(max = 255, message = "The address cannot exceed 255 characters")
        String address,

        @NotBlank(message = "The DNI is mandatory")
        @Length(max = 10, message = "The DNI must have a maximum of 10 characters")
        String dni,

        @NotBlank(message = "The name cannot be empty")
        @Size(min = 1, max = 100, message = "The name must be between 1 and 100 characters")
        String name
) {
}
