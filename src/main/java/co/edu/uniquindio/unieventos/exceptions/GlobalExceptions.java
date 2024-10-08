package co.edu.uniquindio.unieventos.exceptions;

import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.ValidationDTO;
import org.apache.http.protocol.HTTP;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptions {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO<String>> generalException(Exception e) {
        return ResponseEntity.internalServerError().body(new MessageDTO<>(true, e.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDTO<List<ValidationDTO>>> validationException(
            MethodArgumentNotValidException ex ) {
        List<ValidationDTO> errors = new ArrayList<>();
        BindingResult results = ex.getBindingResult();
        for (FieldError e: results.getFieldErrors()) {
            errors.add( new ValidationDTO(e.getField(), e.getDefaultMessage()) );
        }
        return ResponseEntity.badRequest().body( new MessageDTO<>(true, errors) );
    }


    //TODO Here should be all actions
    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<MessageDTO<String>> handleAccountNotActivated(AccountNotActivatedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<MessageDTO<String>> handleInvalidPasswordException(InvalidPasswordException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<MessageDTO<String>> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO<>(true, ex.getMessage()));
    }


    @ExceptionHandler(EmptyShoppingCarException.class)
    public ResponseEntity<MessageDTO<String>> handleEmptyShoppingCar(EmptyShoppingCarException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(InsufficientCapacityException.class)
    public ResponseEntity<MessageDTO<String>> handleInsufficientCapacity(InsufficientCapacityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<MessageDTO<String>> handleOperationNotAllowed(OperationNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageDTO<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<MessageDTO<String>> handleStorageException(StorageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(ValidationCodeException.class)
    public ResponseEntity<MessageDTO<String>> handleValidationCode(ValidationCodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO<>(true, ex.getMessage()));
    }


}

