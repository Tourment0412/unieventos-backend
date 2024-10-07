package co.edu.uniquindio.unieventos.exceptions;

//Excepcion general para cuando no se encuentren recursos
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
