package co.edu.uniquindio.unieventos.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Location {
    @EqualsAndHashCode.Include
    private String name;
    private float price;
    private int maxCapacity;
}
