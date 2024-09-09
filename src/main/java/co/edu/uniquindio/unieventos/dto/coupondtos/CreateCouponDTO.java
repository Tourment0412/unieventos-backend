package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public record CreateCouponDTO(

        @NotBlank float discount,
        @NotBlank LocalDateTime expirationDate,
        @NotBlank CouponType type,
        @NotBlank String name

) {
}
