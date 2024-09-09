package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.coupondtos.*;

public interface CouponSevice {

    CouponInfoDTO getInfoCoupon(String id) throws Exception;

    String createCoupon(CreateCouponDTO coupon) throws Exception;

    String updateCoupon(UpdateCouponDTO coupon) throws Exception;

    String deleteCoupon(String id) throws Exception;



}
