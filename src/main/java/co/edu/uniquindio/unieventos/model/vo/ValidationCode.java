package co.edu.uniquindio.unieventos.model.vo;

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

    public ValidationCode(LocalDateTime creationDate, String code) {
        this.creationDate = creationDate;
        this.code = code;
    }
}
