package co.edu.uniquindio.unieventos.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImagesService {

    String uploadImage(MultipartFile file) throws Exception;
    void deleteImage(String imageName) throws Exception;
}
