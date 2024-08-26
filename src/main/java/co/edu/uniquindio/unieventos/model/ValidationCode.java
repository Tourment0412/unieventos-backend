package co.edu.uniquindio.unieventos.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class ValidationCode {
    private LocalDateTime creationDate;
    private String code;
}
