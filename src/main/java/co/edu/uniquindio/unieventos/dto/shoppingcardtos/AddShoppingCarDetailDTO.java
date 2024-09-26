package co.edu.uniquindio.unieventos.dto.shoppingcardtos;

import co.edu.uniquindio.unieventos.model.vo.Location;

public record AddShoppingCarDetailDTO(

        String idUser,
        //TODO Ask if the id of the event in this DTO can be String maybe is gonna be a token in the future
        String idEvent,
        String locationName,
        int quantity
) {
}
