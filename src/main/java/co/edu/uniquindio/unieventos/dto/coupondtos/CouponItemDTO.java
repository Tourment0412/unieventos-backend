package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;

import java.time.LocalDateTime;

public record CouponItemDTO(
        String id,
        String name,
        CouponType type,
        CouponStatus status,
        LocalDateTime expirationDate,
        float discount
) {
}
