package co.edu.uniquindio.unieventos.dto.coupondtos;

import java.util.List;

public record ListCouponsClient(
        int totalPages,
        List<CouponInfoClientDTO>coupons
) {
}
