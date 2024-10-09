package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.*;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;

import java.util.List;

public interface ShoppingCarService {

<<<<<<< Updated upstream
    /**
     *
     * @param idUser
     * @return
     * @throws Exception
     */
    String createShoppingCar(String idUser) throws Exception;
=======

    ShoppingCar createShoppingCar(String idUser) throws Exception;
>>>>>>> Stashed changes

    /**
     *
     * @param addShoppingCarDetailDTO
     * @return
     * @throws Exception
     */
    String addShoppingCarDetail(AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws Exception;

    /**
     *
     * @param deleteCarDetailDTO
     * @return
     * @throws Exception
     */
    String deleteShoppingCarDetail(DeleteCarDetailDTO deleteCarDetailDTO) throws Exception;

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<CarItemViewDTO> listShoppingCarDetails(String userId) throws Exception;

    /**
     *
     * @param editCarDetailDTO
     * @return
     * @throws Exception
     */
    //TODO Ask if maybe a edit option would be good or something.
    String editCarDetail (EditCarDetailDTO editCarDetailDTO) throws Exception;

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    ShoppingCar getShoppingCar(String userId) throws Exception;

    /**
     *
     * @param idUser
     * @throws Exception
     */
    void deleteShoppingCar(String idUser) throws Exception;
}
