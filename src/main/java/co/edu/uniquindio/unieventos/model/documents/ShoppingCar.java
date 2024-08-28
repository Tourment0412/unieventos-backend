package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.vo.CarDetail;
import lombok.*;
import  java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("shoppingCars")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ShoppingCar {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private LocalDateTime date;
    private List<CarDetail> items;
    private ObjectId userId;

}
