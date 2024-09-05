package co.edu.uniquindio.unieventos.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateAccountDTO(
        //This is validation for the fields, are going to throw an exception with this message
        @NotBlank(message = "La cedula es obligatoria") @Length(max = 10, message = "La cedula debe " +
                "tener maximo 10 caracteres")
        String dni,

        @NotBlank @Length(max = 50) String name,
        @Length(max = 50) String phoneNumber,
        @Length(max = 100) String address,
        @NotBlank @Length(max = 50) String email,
        @NotBlank @Length(min = 7,max = 20) String password) {


}
