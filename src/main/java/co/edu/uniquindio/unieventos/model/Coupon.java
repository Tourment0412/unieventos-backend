package co.edu.uniquindio.unieventos.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("coupons")
@Setter
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Coupon {
    @Id
    @EqualsAndHashCode.Include
    private int id;
    private String name;
    private float discount;
    private LocalDateTime expirationDate;
    private CouponType type;
}
