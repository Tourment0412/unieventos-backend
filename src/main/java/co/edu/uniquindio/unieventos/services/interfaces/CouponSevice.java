package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.coupondtos.*;
import co.edu.uniquindio.unieventos.model.documents.Coupon;

public interface CouponSevice {

    CouponInfoDTO getInfoCoupon(String id) throws Exception;

    String createCoupon(CreateCouponDTO coupon) throws Exception;

    String updateCoupon(UpdateCouponDTO coupon) throws Exception;

    String deleteCoupon(String id) throws Exception;

    Coupon getCouponById(String id) throws Exception;

}
