package co.edu.uniquindio.unieventos.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Account {
    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String email;
    private String password;
}
