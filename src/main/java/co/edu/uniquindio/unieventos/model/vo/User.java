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
}
