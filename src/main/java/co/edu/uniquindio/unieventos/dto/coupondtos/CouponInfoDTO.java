package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CouponInfoDTO(
        String id,
        String name,
        CouponType type,
        CouponStatus status,
        String code,
        LocalDateTime expirationDate,
        float discount

) {
}
