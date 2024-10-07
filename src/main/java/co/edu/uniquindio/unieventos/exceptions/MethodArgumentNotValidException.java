package co.edu.uniquindio.unieventos.exceptions;

//Excepcion que se podría mostrar cuando se ingresan campos en formatos no validos o faltantes
public class MethodArgumentNotValidException extends Exception {

    public MethodArgumentNotValidException(String message) {
        super(message);
    }
}
