package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.emaildtos.EmailDTO;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.util.Properties;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


@Service
public class EmailServiceImp implements EmailService {

    @Value("${simplejavamail.smtp.host}")
    private String SMTP_HOST;

    @Value("${simplejavamail.smtp.port}")
    private int SMTP_PORT;

    @Value("${simplejavamail.smtp.username}")
    private String SMTP_USERNAME;

    @Value("${simplejavamail.smtp.password}")
    private String SMTP_PASSWORD;

    @Override
    public void sendEmail(EmailDTO emailDTO) throws Exception {
        Email email = EmailBuilder.startingBlank()
                .from(SMTP_USERNAME)
                .to(emailDTO.receiver())
                .withSubject(emailDTO.subject())
                //This plain text could be replaced with "withHTMLText"
                .withPlainText(emailDTO.body())
                .buildEmail();


        try (Mailer mailer = MailerBuilder
                .withSMTPServer(SMTP_HOST, SMTP_PORT, SMTP_USERNAME, SMTP_PASSWORD)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {


            mailer.sendMail(email);
        }


    }
/*
    public void sendEmailWithInlineImage(EmailDTO emailDTO, String qrFilePath) throws Exception {
        // Configuración de propiedades para la sesión de correo
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST); // Corregido: SMTP_HOST en lugar de SMTP_PORT
        props.put("mail.smtp.port", SMTP_PORT);

        // Crear una sesión de correo
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        // Crear un nuevo mensaje
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USERNAME)); // Cambiado smtpUser a SMTP_USERNAME
        message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailDTO.receiver()));
        message.setSubject(emailDTO.subject());

        // Crear el cuerpo del mensaje
        MimeMultipart multipart = new MimeMultipart("related");

        // Parte del texto del mensaje
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(emailDTO.body(), "text/html");
        multipart.addBodyPart(textPart);

        // Parte de la imagen (código QR)
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource fds = new FileDataSource(new File(qrFilePath));
        imagePart.setDataHandler(new javax.activation.DataHandler(fds)); // Usar setDataHandler en lugar de setDataSource
        imagePart.setDisposition(MimeBodyPart.INLINE);
        imagePart.setHeader("Content-ID", "<qrCodeImage>"); // Referencia en el cuerpo del mensaje
        multipart.addBodyPart(imagePart);

        // Establecer el contenido del mensaje
        message.setContent(multipart);

        // Enviar el mensaje
        Transport.send(message);
    }

 */
}
