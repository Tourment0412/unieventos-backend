package co.edu.uniquindio.unieventos.dto.accountdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

public record CreateAccountDTO(
        //This is validation for the fields, are going to throw an exception with this message
        @NotBlank(message = "The DNI is mandatory")
        @Size(max = 10, message = "The DNI must have a maximum of 10 characters")
        String dni,

        @NotBlank(message = "The name is mandatory")
        @Size(max = 50, message = "The name must have a maximum of 50 characters")
        String name,

        @Size(min = 10, max = 15, message = "The phone number must be between 10 and 15 characters long")
        String phoneNumber,

        @Size(max = 100, message = "The address must have a maximum of 100 characters")
        String address,

        @NotBlank(message = "Email can't be empty")
        @Email(message = "Email format is invalid")
        String email,

        @NotBlank(message = "The password is mandatory")
        @Size(min = 7, max = 20, message = "The password must be between 7 and 20 characters long")
        String password
) {}
