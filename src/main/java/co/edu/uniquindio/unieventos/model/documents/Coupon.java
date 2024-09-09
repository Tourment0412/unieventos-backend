package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import lombok.*;
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
    private String id;

    private String name;
    private CouponType type;
    private CouponStatus status;
    private String code;
    private LocalDateTime expirationDate;
    private float discount;

    @Builder
    public Coupon(String name, CouponType type, CouponStatus status, String code, LocalDateTime expirationDate,
                  float discount) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.code = code;
        this.expirationDate = expirationDate;
        this.discount = discount;
    }

    public Coupon() {

    }
}
