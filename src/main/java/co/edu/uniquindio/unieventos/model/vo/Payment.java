package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Payment {
    @EqualsAndHashCode.Include
    private String id;

    private String currency; //Moneda (Dollar, Euro, ETC)
    private String paymentType;//Tipo de pago
    private String statusDetail;
    private String authorizationCode;
    private LocalDateTime date;
    private float transactionValue;
    private String status;

    @Builder
    public Payment(String id, String currency, String paymentMethod, String statusDetail,
                   String authorizationCode, LocalDateTime date, float transactionValue, String status) {
        this.id = id;
        this.currency = currency;
        this.paymentType = paymentMethod;
        this.statusDetail = statusDetail;
        this.authorizationCode = authorizationCode;
        this.date = date;
        this.transactionValue = transactionValue;
        this.status = status;
    }
}
