package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.*;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;

import java.util.List;

public interface ShoppingCarService {


    String createShoppingCar(CreateShoppingCarDTO createShoppingCarDTO) throws Exception;

    String addShoppingCarDetail(AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws Exception;
    void deleteShoppingCarDetail(DeleteCarDetailDTO deleteCarDetailDTO) throws Exception;
    List<CarItemViewDTO> listShoppingCarDetails(String userId) throws Exception;
    //TODO Ask if maybe a edit option would be good or something.
    void editCarDetail (EditCarDetailDTO editCarDetailDTO) throws Exception;
    ShoppingCar getShoppingCar(String userId) throws Exception;
}
