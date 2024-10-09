package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.*;
import co.edu.uniquindio.unieventos.exceptions.EmptyShoppingCarException;
import co.edu.uniquindio.unieventos.exceptions.InsufficientCapacityException;
import co.edu.uniquindio.unieventos.exceptions.OperationNotAllowedException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;

import java.util.List;

public interface ShoppingCarService {


    /**
     *
     * @param idUser
     * @return
     * @throws Exception
     */
    ShoppingCar createShoppingCar(String idUser);


    /**
     *
     * @param addShoppingCarDetailDTO
     * @return
     * @throws Exception
     */
    String addShoppingCarDetail(AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws ResourceNotFoundException, OperationNotAllowedException, InsufficientCapacityException;

    /**
     *
     * @param deleteCarDetailDTO
     * @return
     * @throws Exception
     */
    String deleteShoppingCarDetail(DeleteCarDetailDTO deleteCarDetailDTO) throws EmptyShoppingCarException;

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<CarItemViewDTO> listShoppingCarDetails(String userId) throws EmptyShoppingCarException;

    /**
     *
     * @param editCarDetailDTO
     * @return
     * @throws Exception
     */
    //TODO Ask if maybe a edit option would be good or something.
    String editCarDetail (EditCarDetailDTO editCarDetailDTO) throws EmptyShoppingCarException;

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    ShoppingCar getShoppingCar(String userId) throws EmptyShoppingCarException;

    /**
     *
     * @param idUser
     * @throws Exception
     */
    void deleteShoppingCar(String idUser) throws EmptyShoppingCarException;
}
