package co.edu.uniquindio.unieventos.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("admins")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Admin extends Account {
    @Id
    private String code;
}
