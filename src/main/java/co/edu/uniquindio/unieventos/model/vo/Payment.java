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
    private String paymentMethod;//Tipo de pago
    private String statusDetail;
    private String authorizationCode;
    private LocalDateTime date;
    private float transactionValue;
    private String status;
}
