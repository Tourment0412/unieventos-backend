package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.ValidationCode;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("accounts")
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private ObjectId userId;
    private String email;
    private String password;
    private ValidationCode RegistrationValidationCode;
    private Role role;
    private LocalDateTime RegistrationDate;
    private AccountStatus status;
    private ValidationCode PasswordValidationCode;


}
