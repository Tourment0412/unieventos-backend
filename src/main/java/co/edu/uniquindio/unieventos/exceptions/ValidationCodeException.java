package co.edu.uniquindio.unieventos.exceptions;

//Excepcion que podemos utilizar en el proseso de restablecimiento de cotraseña, como por ejemplo si el codigo ingresado ya expiró
public class ValidationCodeException extends Exception {

    public ValidationCodeException(String message) {
        super(message);
    }
}
