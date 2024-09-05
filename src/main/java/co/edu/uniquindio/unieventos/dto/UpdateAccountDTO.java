package co.edu.uniquindio.unieventos.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateAccountDTO(
        //This refers to the MongoDB Id
        @NotBlank String id,
        @NotBlank @Length(max = 50) String name,
        @NotBlank @Length(max = 50) String phoneNumber,
        @NotBlank @Length(max = 50) String address,
        @NotBlank @Length(min = 7,max = 20) String password
        ) {
}
