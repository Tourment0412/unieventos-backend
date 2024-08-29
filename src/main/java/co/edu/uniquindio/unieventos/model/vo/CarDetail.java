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

    @Builder
    public CarDetail(int amount, String locationName, ObjectId idEvent) {
        this.amount = amount;
        this.locationName = locationName;
        this.idEvent = idEvent;
    }
}
