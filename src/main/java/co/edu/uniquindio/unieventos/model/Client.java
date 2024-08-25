package co.edu.uniquindio.unieventos.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("clients")
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client extends Account {
    @Id
    @EqualsAndHashCode.Include
    private String code;
    private String address;
    private String phone;

    //Falta la relacion con
    //private Coupon coupon (o Lista de cupon)

}
