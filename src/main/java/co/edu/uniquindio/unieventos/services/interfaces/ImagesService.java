package co.edu.uniquindio.unieventos.services.interfaces;

import co.edu.uniquindio.unieventos.exceptions.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImagesService {

    /**
     *
     * @param file
     * @return
     * @throws Exception
     */
    String uploadImage(MultipartFile file) throws Exception;

    /**
     *
     * @param imageName
     * @return
     * @throws Exception
     */
    String deleteImage(String imageName) throws StorageException;
}
