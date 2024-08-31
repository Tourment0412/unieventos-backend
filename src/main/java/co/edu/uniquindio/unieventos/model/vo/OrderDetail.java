package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class OrderDetail {
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId eventId;
    private float price;
    private String locationName;
    private int quantity;

    @Builder
    public OrderDetail(String id, ObjectId idEvent, float price, String locationName, int quantity) {
        this.id = id;
        this.eventId = idEvent;
        this.price = price;
        this.locationName = locationName;
        this.quantity = quantity;
    }
}
