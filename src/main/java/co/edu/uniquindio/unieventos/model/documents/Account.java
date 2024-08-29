package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.User;
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

    private User user;
    private String email;
    private String password;
    private ValidationCode registrationValidationCode;
    private Role role;
    private LocalDateTime registrationDate;
    private AccountStatus status;
    private ValidationCode passwordValidationCode;

    //This was created in this way for the test
    @Builder
    public Account(User user, String email, String password,ValidationCode registrationValidationCode, Role role,
                   LocalDateTime registrationDate, AccountStatus status, ValidationCode passwordValidationCode) {
        this.user = user;
        this.email = email;
        this.password = password;
        this.registrationValidationCode = registrationValidationCode;
        this.role = role;
        this.registrationDate = registrationDate;
        this.status = status;
        this.passwordValidationCode = passwordValidationCode;

    }
}
