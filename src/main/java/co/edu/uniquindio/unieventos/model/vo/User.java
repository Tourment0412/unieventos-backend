package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class User {
    private String phoneNumber;
    private String address;
    private String dni;
    private String name;

    @Builder
    public User(String phoneNumber, String adress, String dni, String name) {
        this.phoneNumber = phoneNumber;
        this.address = adress;
        this.dni = dni;
        this.name = name;
    }
}
