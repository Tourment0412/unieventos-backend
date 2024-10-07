package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record UpdateCouponDTO(
        @NotNull(message = "ID cannot be null")
        String id,

        @Min(value = 0, message = "Discount must be at least 0%")
        @Max(value = 1, message = "Discount must be at most 100%")
        float discount,

        @Future(message = "Expiration date must be in the future")
        @NotNull(message = "Expiration date is required")
        LocalDateTime expirationDate,

        @NotNull(message = "Coupon type is required")
        CouponType type,

        @NotBlank(message = "Coupon name cannot be empty")
        @Length(max = 50, message = "Coupon name must not exceed 50 characters")
        String name

) {
}
