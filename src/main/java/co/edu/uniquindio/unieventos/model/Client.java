package co.edu.uniquindio.unieventos.model;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("clients")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Client extends Account {

    private String address;
    private String phone;
    //No termino de entender para que sirve este atributo
    private String code;

    //Falta la relacion con
    //private Coupon coupon (o Lista de cupon)

}
