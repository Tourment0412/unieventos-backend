package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("events")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String address;
    private String city;
    private String poster;//Imagen
    private String locationDistribution;//Imagen
    private LocalDateTime date;


    private List<Location> locations;
}
