package co.edu.uniquindio.unieventos.model;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("clients")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Client extends Account {
    @Id
    private String code;
    private String address;
    private String phone;

    //Relations
    //TODO we have doubts on this should it be a document on de DB or not
    //Tambien se ha decido que la relacion este aca ya que se puede aplicar a compras
    private ObjectId couponClient;
}
