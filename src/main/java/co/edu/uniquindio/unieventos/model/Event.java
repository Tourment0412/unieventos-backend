package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private String poster;
    private String locationDistribution;
    private LocalDateTime date;
}
