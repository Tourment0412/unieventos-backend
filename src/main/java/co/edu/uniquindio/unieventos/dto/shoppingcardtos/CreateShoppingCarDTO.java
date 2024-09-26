package co.edu.uniquindio.unieventos.dto.shoppingcardtos;

import co.edu.uniquindio.unieventos.model.vo.CarDetail;

import java.util.List;

public record CreateShoppingCarDTO(

        //TODO ask if this is going to be a token in the future (In the class this is an ObjectId)
        String accountId,
        List<CarDetail> items

) {
}
