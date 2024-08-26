package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("Orders")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @EqualsAndHashCode.Include
    private int id;

    private int ticketsQuantity;
    private float total;
    private int units;
    private float individualPrice;
    private LocalDateTime date;

    private Location location;

    //Relations
    private ObjectId client;
    private ObjectId event;

    //This one may be optional on a purchase (Depends on the event, and the user)
    private ObjectId coupon;
}
