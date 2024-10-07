package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateOrderDTO(
        @NotNull(message = "The 'isForFriend' field cannot be null")
        boolean isForFriend,


        @Email(message = "Friend's email must be a valid email address")
        @Length(max = 50, message = "Friend's email must not exceed 50 characters")
        String friendEmail,

        @NotBlank(message = "Client ID cannot be empty")
        String clientId,


        String counponCode
) {



}
