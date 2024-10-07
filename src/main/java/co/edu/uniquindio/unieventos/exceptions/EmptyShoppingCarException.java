package co.edu.uniquindio.unieventos.exceptions;

//Pensada par cuando se intente crear una orden y el carro esté vacio
public class EmptyShoppingCarException extends Exception {

    public EmptyShoppingCarException(String message) {
        super(message);
    }
}
