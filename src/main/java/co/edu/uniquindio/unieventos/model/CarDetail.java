package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CarDetail {
    private int amount;
    private String locationName;
    private ObjectId idEvent;
}
