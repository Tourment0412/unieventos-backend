package co.edu.uniquindio.unieventos.model.vo;

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
    private int maxCapacity;//COMN

    @Builder
    public Location(String name, float price, int maxCapacity) {
        this.name = name;
        this.price = price;
        this.maxCapacity = maxCapacity;
    }
}
