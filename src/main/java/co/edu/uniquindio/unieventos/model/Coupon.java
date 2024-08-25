package co.edu.uniquindio.unieventos.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Coupon {

    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private float discount;
    private LocalDateTime expirationDate;
    private CouponType type;
}
