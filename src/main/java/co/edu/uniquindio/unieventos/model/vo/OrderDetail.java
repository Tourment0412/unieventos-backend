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

    private ObjectId idEvent;
    private float price;
    private String locationName;
    private int quantity;

}
