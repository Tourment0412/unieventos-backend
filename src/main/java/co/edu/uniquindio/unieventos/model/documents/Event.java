package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
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
    private String coverImage;//Imagen portada (poster)
    private String locationDistribution;//Imagen
    private LocalDateTime date;
    //Added based on the map given by teacher
    private String description;


    private EventType type;
    private EventStatus status;
    private List<Location> locations;
}
