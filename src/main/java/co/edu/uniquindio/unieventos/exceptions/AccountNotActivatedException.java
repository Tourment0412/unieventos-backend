package co.edu.uniquindio.unieventos.exceptions;

//Puede ser usada cuando se intente iniciar sesion a una cuenta inactiva
public class AccountNotActivatedException extends Exception {

    public AccountNotActivatedException(String message) {
        super(message);
    }
}
