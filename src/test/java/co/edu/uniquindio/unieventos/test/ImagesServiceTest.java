package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.services.implementations.ImagesServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;


@SpringBootTest
public class ImagesServiceTest {

    @Autowired
    private ImagesServiceImp imagesServiceImp;

    @Test
    public void testUploadImage() throws Exception {
        // Path of the local image for the test
        File file = new File("C:\\Users\\NITRO 5\\Pictures\\Captura de pantalla 2024-09-07 170937.png");

        /*
            Create a MultipartFile Mock based on local file
            An instance of MockMultipartFile is created, which simulates the behavior of a MultipartFile
            (file sent through an HTTP request), facilitating testing without the need for a real request.
        */

        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(
                "file",                        // Nombre del archivo
                file.getName(),                 // Nombre original
                "image/png",                    // Tipo de contenido (MIME type)
                inputStream                     // Contenido del archivo
        );
        System.out.println(file.getName());
        // Call to service method to upload the image
        String imageUrl = imagesServiceImp.uploadImage(multipartFile);

        // Verify that a correct Firebase URL has been generated
        Assertions.assertNotNull(imageUrl);  // Ensure that the URL is not null
        Assertions.assertTrue(imageUrl.contains("https://firebasestorage.googleapis.com"));

        System.out.println("Imagen subida exitosamente: " + imageUrl);
    }

    @Test
    public void testDeleteImage() throws Exception {
        //Name of the file on the storage bucket (this gonna change when you delete and repost the image)
        String imageName = "c27524a7-b7fd-4b37-ba0f-2ec4344a2667-Captura de pantalla 2024-09-07 170937.png";

        // Call to service method to  the image
        imagesServiceImp.deleteImage(imageName);

        // No debería lanzar excepciones, por lo que simplemente validamos que no haya fallos
        System.out.println("Imagen eliminada exitosamente.");
    }
}
