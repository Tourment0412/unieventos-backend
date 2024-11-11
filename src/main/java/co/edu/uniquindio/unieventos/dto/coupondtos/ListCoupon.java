package co.edu.uniquindio.unieventos.dto.coupondtos;



import java.util.List;

public record ListCoupon(
        int totalPages,
        List<CouponItemDTO> coupons
) {
}
