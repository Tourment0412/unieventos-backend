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
    private LocalDateTime orderDate;
    private float total;

    //Relations
    private ObjectId clientId;
    private ObjectId couponId;
    private Payment payment;
}
