package co.edu.uniquindio.unieventos.exceptions;

//Podemos utilizarla en cualquier porblema en ala subida o eliminacion de imagenes
public class StorageException extends Exception {

    public StorageException(String message) {
        super(message);
    }
}
