package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private String id;
    private String phoneNumber;
    private String adress;
    private String dni;
    private String name;

    @Builder
    public User(String id, String phoneNumber, String adress, String dni, String name) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.dni = dni;
        this.name = name;
    }
}
