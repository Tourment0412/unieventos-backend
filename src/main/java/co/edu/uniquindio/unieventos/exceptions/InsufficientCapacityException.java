package co.edu.uniquindio.unieventos.exceptions;

//Se puede utilizar cuando se intenta comprar entradas de una localidad y no hay suficientes
public class InsufficientCapacityException extends Exception {

    public InsufficientCapacityException(String message) {
        super(message);
    }
}
