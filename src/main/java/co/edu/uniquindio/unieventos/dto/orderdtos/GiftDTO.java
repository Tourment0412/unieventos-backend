package co.edu.uniquindio.unieventos.dto.orderdtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record GiftDTO (
        @Email(message = "Friend's email must be a valid email address")
        @Length(max = 50, message = "Friend's email must not exceed 50 characters")
        String friendEmail,

        @NotBlank(message = "Order ID cannot be empty")
        String idOrder
){
}
