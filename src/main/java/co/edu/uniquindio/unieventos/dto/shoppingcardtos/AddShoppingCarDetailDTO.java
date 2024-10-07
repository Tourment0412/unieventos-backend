package co.edu.uniquindio.unieventos.dto.shoppingcardtos;

import co.edu.uniquindio.unieventos.model.vo.Location;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddShoppingCarDetailDTO(

        @NotBlank(message = "User ID cannot be empty")
        String idUser,

        @NotBlank(message = "Event ID cannot be empty")
        String idEvent,

        @NotBlank(message = "Location name cannot be empty")
        String locationName,

        @Min(value = 1, message = "Quantity must be at least 1")
        int quantity
) {
}
