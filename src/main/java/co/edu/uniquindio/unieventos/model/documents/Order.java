package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("orders")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    private List<OrderDetail> items;
    private String gatewayCode;//codigo pasarela
    private LocalDateTime date;
    private float total;
    //Relations
    private ObjectId clientId;
    private ObjectId couponId;
    private Payment payment;

    @Builder
    //I think it is better to have the objectID parametters as strings and cast them on ObjectId inside the builder
    public Order(List<OrderDetail> items, String gatewayCode, LocalDateTime orderDate, float total,
                 ObjectId clientId, ObjectId couponId, Payment payment) {
        this.items = items;
        this.gatewayCode = gatewayCode;
        this.date = orderDate;
        this.total = total;
        this.clientId = clientId;
        this.couponId = couponId;
        this.payment = payment;
    }
}
