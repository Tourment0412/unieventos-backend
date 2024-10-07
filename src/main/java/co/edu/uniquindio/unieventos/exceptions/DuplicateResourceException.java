package co.edu.uniquindio.unieventos.exceptions;

//Excepcion que se puede utilizar cuando halla una violacion a la integridad de los datos, como por ejemplo crear un evento con un nombre que ya existe, aplicaria igual para cupone y cuentas
public class DuplicateResourceException extends Exception {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
