package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;

import java.time.LocalDateTime;

public record CouponItemClientDTO(
        String id,
        String name,
        CouponType type,
        LocalDateTime expirationDate,
        float discount
) {
}
