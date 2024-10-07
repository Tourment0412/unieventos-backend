package co.edu.uniquindio.unieventos.exceptions;

//Se puede usar con al presentar problemas al enviar el correo
public class EmailSendingException extends Exception {

    public EmailSendingException(String message) {
        super(message);
    }
}
