package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.exceptions.DuplicateResourceException;
import co.edu.uniquindio.unieventos.exceptions.ResourceNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponType;

import java.util.List;

public interface CouponService {

    /**
     * Gets the information of a coupon
     * @param id id of coupon
     * @return The coupon information
     * @throws Exception
     */
    CouponInfoDTO getInfoCouponAdmin(String id) throws ResourceNotFoundException;

    /**
     * Gets a avaliable coupon
     * @param id id of coupon
     * @return The coupon information
     * @throws Exception
     */
    CouponInfoClientDTO getCouponClient(String id) throws ResourceNotFoundException;

    /**
     * Creates a coupon
     * @param coupon Data of the coupon that will be created
     * @return The id of the coupon
     * @throws Exception
     */
    String createCoupon(CreateCouponDTO coupon) throws DuplicateResourceException;

    /**
     * Updates a coupon
     * @param coupon Data of the coupon that will be updated
     * @return The id of the coupon
     * @throws Exception
     */
    String updateCoupon(UpdateCouponDTO coupon) throws ResourceNotFoundException;

    /**
     * Deletes a coupon
     * @param id id of coupon
     * @return The id of the coupon
     * @throws Exception
     */
    String deleteCoupon(String id) throws ResourceNotFoundException;

    /**
     * Gets a coupon by its id
     * @param id id of coupon
     * @return Coupon wth the specified id
     * @throws Exception
     */
    Coupon getCouponById(String id) throws ResourceNotFoundException;

    /**
     * Gests all the coupons specified by the pagination information
     * @param page The number of the page
     * @return The coupons specified
     */
    ListCoupon getAllCouponsAdmin(int page);

    /**
     * Gests all the available coupons specified by the pagination information
     * @param page The number of the page
     * @return The available coupons specified
     */
    List<CouponItemClientDTO> getAllCouponsClient(int page);

    /**
     * Gets a coupon by its code
     * @param s Code of coupon
     * @return Coupon with the specified code
     * @throws Exception
     */
    Coupon getCouponByCode(String s) throws ResourceNotFoundException;

    /**
     * Gets a coupon by its  an avali
     * @param code Code of coupon
     * @return Coupon with the specified code
     * @throws Exception
     */
    CouponInfoClientDTO getCouponClientCode(String code) throws ResourceNotFoundException;

    List<CouponType> getCouponTypes() throws Exception;
}
