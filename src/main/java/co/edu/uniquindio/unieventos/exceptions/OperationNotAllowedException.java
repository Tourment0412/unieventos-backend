package co.edu.uniquindio.unieventos.exceptions;

//Está seria para cuando se intente hace una operacion no permitida como acceder a una cuanta eliminada o inactica
public class OperationNotAllowedException extends Exception {

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
