package co.edu.uniquindio.unieventos.exceptions;

//Algo especifica pero puede ser utilizada en caso de que el formato del email ingresado no sea correcto
public class InvalidTextStructureException extends Exception {

    public InvalidTextStructureException(String message) {
        super(message);
    }
}
