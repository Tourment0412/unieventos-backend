package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.dto.emaildtos.EmailDTO;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO) throws Exception;
}
