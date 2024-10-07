package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.emaildtos.EmailDTO;

import java.io.IOException;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO) throws Exception;
    void sendEmailHtmlWithAttachment(EmailDTO emailDTO, byte[] qrCodeImage, String qrCodeContentId) throws Exception;
    byte[] downloadImage(String imageUrl) throws IOException;
}
