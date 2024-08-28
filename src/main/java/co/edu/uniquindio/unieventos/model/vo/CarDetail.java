package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class CarDetail {
    private int amount;
    private String locationName;
    private ObjectId idEvent;
}
