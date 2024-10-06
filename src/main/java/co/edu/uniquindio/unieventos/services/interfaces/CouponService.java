package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.model.documents.Coupon;

import java.util.List;

public interface CouponService {

    CouponInfoDTO getInfoCouponAdmin(String id) throws Exception;

    CouponInfoClientDTO getCouponClient(String id) throws Exception;

    String createCoupon(CreateCouponDTO coupon) throws Exception;

    String updateCoupon(UpdateCouponDTO coupon) throws Exception;

    String deleteCoupon(String id) throws Exception;

    Coupon getCouponById(String id) throws Exception;

    List<CouponItemDTO> getAllCouponsAdmin(int page) throws Exception;
    List<CouponItemClientDTO> getAllCouponsClient(int page) throws Exception;

}
