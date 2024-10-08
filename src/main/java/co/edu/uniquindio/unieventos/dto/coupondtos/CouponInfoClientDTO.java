package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CouponInfoClientDTO(
        String id,
        String name,
        CouponType type,
        String code,
        LocalDateTime expirationDate,
        float discount
) {
}
