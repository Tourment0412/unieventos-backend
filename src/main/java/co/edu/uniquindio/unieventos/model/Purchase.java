package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("purchases")
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Purchase {
    @Id
    @EqualsAndHashCode.Include
    private int id;

    private int ticketsQuantity;
    private float total;
    private int units;
    private float individualPrice;
    private LocalDateTime date;


    //Relaciones
    //private Location location; (Localidad)
    //private String codigoEvento
}
