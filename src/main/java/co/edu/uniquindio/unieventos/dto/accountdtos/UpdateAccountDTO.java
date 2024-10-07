package co.edu.uniquindio.unieventos.dto.accountdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record UpdateAccountDTO(
        //This refers to the MongoDB Id
        @NotNull(message = "ID cannot be null")
        String id,

        @NotBlank(message = "The name cannot be empty")
        @Size(min = 1, max = 100, message = "The name must be between 1 and 100 characters")
        String name,

        @Size(min = 10, max = 15, message = "The phone number must be between 10 and 15 characters long")
        String phoneNumber,

        @Size(max = 255, message = "The address cannot exceed 255 characters")
        String address,

        @NotBlank(message = "The password is mandatory")
        @Size(min = 7, max = 20, message = "The password must be between 7 and 20 characters long")
        String password
        ) {
}
