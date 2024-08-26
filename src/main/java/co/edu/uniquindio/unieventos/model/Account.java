package co.edu.uniquindio.unieventos.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("accounts")
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @EqualsAndHashCode.Include
    private String id;//Cedula
    private ObjectId userId;
    private String email;
    private String password;
    private ValidationCode RegistrationValidationCode;
    private Role role;
    private LocalDateTime RegistrationDate;
    private AccountStatus status;
    private ValidationCode PasswordValidationCode;


}
