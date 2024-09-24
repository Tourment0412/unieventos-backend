package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.services.interfaces.ImagesService;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ImagesServiceImp implements ImagesService {
    @Override
    public String uploadImage(MultipartFile multipartFile) throws Exception {
        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = String.format("%s-%s", UUID.randomUUID().toString(),
                multipartFile.getOriginalFilename());
        Blob blob = bucket.create(fileName, multipartFile.getInputStream(),
                multipartFile.getContentType());
        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                blob.getName()
        );
    }

    @Override
    public void deleteImage(String imageName) throws Exception {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(imageName);
        blob.delete();
    }
}
