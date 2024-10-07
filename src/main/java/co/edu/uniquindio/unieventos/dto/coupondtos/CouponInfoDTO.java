package co.edu.uniquindio.unieventos.dto.coupondtos;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record CouponInfoDTO(
        @NotNull(message = "ID cannot be null")
        String id,

        @NotBlank(message = "Coupon name cannot be empty")
        @Length(max = 50, message = "Coupon name must not exceed 50 characters")
        String name,

        @NotNull(message = "Coupon type is required")
        CouponType type,

        @NotNull(message = "Coupon status is required")
        CouponStatus status,

        @NotBlank(message = "Coupon code cannot be empty")
        @Length(max = 6, message = "Coupon code must not exceed 6 characters")
        String code,

        @Future(message = "Expiration date must be in the future")
        @NotNull(message = "Expiration date is required")
        LocalDateTime expirationDate,

        @Min(value = 0, message = "Discount must be at least 0%")
        @Max(value = 1, message = "Discount must be at most 100%")
        float discount

) {
}
