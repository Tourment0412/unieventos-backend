package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.AddShoppingCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.DeleteCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.CarItemViewDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.repositories.EventRepo;
import co.edu.uniquindio.unieventos.repositories.ShoppingCarRepo;
import co.edu.uniquindio.unieventos.services.implementations.ShoppingCarServiceImp;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import io.jsonwebtoken.lang.Assert;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ShoppingCarServiceTest {

    @Autowired
    private ShoppingCarRepo shoppingCarRepo;

    @Autowired
    private EventService eventService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private EventRepo eventRepo;

    @BeforeEach
    void setUp() {
        //shoppingCarRepo.deleteAll(); // Limpiar el repositorio antes de cada prueba
    }


    @Test
    public void testCreateShoppingCar() throws Exception {
        // Arrange
        String userId = "66d082d1f1f27b1e5b8e1339"; // ID de usuario válido
        ShoppingCar existingShoppingCar = shoppingCarRepo.findByUserId(new ObjectId(userId)).orElse(null);

        // Si ya existe un carrito para este usuario, se debe eliminar para asegurar que no haya conflicto en el test
        if (existingShoppingCar != null) {
            shoppingCarRepo.delete(existingShoppingCar);
        }

        // Act
        String carId = shoppingCarService.createShoppingCar(userId);

        // Assert
        assertNotNull(carId, "El ID del carrito no debería ser nulo");

        // Verificar que el carrito fue creado correctamente en la base de datos
        Optional<ShoppingCar> createdShoppingCar = shoppingCarRepo.findById(carId);
        Assertions.assertTrue(createdShoppingCar.isPresent(), "El carrito debería existir en la base de datos");

        // Verificar los detalles del carrito
        ShoppingCar shoppingCar = createdShoppingCar.get();
        assertEquals(userId, shoppingCar.getUserId().toString(), "El ID del usuario debería coincidir");
        assertTrue(shoppingCar.getItems().isEmpty(), "El carrito debería estar vacío inicialmente");
        assertNotNull(shoppingCar.getDate(), "La fecha de creación del carrito no debería ser nula");
    }


    @Test
    public void testAddShoppingCarDetail() throws Exception {
        // Arrange
        String userId = "66d082d1f1f27b1e5b8e1339"; // ID del usuario
        String eventId = "66f9e4d542ac3e6f6a3d7672"; // ID del evento

        // DTO para agregar detalles al carrito
        Event event = eventRepo.findById(eventId).get();
        Location location = event.findLocationByName("Locación 1");
        AddShoppingCarDetailDTO addShoppingCarDetailDTO = new AddShoppingCarDetailDTO(userId, eventId, location.getName(), 2);

        // Act
        String carId = shoppingCarService.addShoppingCarDetail(addShoppingCarDetailDTO);

        // Assert
        assertNotNull(carId);
        ShoppingCar updatedShoppingCar = shoppingCarRepo.findById(carId).orElse(null);
        assertNotNull(updatedShoppingCar); // Verificar que el carrito se haya recuperado
        assertFalse(updatedShoppingCar.getItems().isEmpty()); // Verificar que el carrito no esté vacío
        assertEquals(carId, updatedShoppingCar.getId()); // Verificar que el ID coincida
    }


    @Test
    public void testDeleteShoppingCarDetail() throws Exception {
        // Arrange
        String userId = "66d082d1f1f27b1e5b8e1339"; // ID del usuario
        String eventId = "66f9e4d542ac3e6f6a3d7672"; // ID del evento
        DeleteCarDetailDTO deleteCarDetailDTO = new DeleteCarDetailDTO(userId, eventId, "Locación 2");
        List<CarDetail> carDetails = new ArrayList<>();
        CarDetail carDetail = new CarDetail();
        carDetail.setIdEvent(new ObjectId(eventId));
        carDetail.setLocationName("Locación 2");
        carDetails.add(carDetail);

        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.setItems(carDetails);
        shoppingCarRepo.save(shoppingCar); // Guardar el carrito directamente

        // Act
        shoppingCarService.deleteShoppingCarDetail(deleteCarDetailDTO);

        // Assert
        // Verificar que el carrito de compras aún exista
        ShoppingCar updatedShoppingCar = shoppingCarRepo.findById("carId").orElse(null);
        assertNotNull(updatedShoppingCar, "El carrito de compras debería existir después de la eliminación");

        // Verificar que el detalle del carrito haya sido eliminado
        assertFalse(updatedShoppingCar.getItems().stream()
                        .anyMatch(item -> item.getLocationName().equals("Locación 2")),
                "El detalle del carrito con 'Locación 2' debería haber sido eliminado");

        // Verificar que la lista de detalles esté vacía si era el único elemento
        // assertTrue(updatedShoppingCar.getItems().isEmpty(), "La lista de detalles del carrito debería estar vacía");

    }

    @Test
    public void testListShoppingCarDetails() throws Exception {
        // Arrange
        String userId = "66d082d1f1f27b1e5b8e1339"; // ID del usuario
        String eventId = "66f9e4d542ac3e6f6a3d7672"; // ID del evento
        ShoppingCar shoppingCar = new ShoppingCar();
        List<CarDetail> carDetails = new ArrayList<>();
        CarDetail carDetail = new CarDetail();
        carDetail.setIdEvent(new ObjectId(eventId));
        carDetail.setLocationName("Locación 1");
        carDetails.add(carDetail);
        shoppingCar.setItems(carDetails);
        shoppingCarRepo.save(shoppingCar); // Guardar el carrito

        Event event = new Event();
        event.setName("Evento 1");
        event.setType(EventType.CONCERT);
        eventRepo.save(event); // Guardar el evento

        Location location = new Location();
        location.setPrice(100.0F);
        event.getLocations().add(location);

        // Act
        List<CarItemViewDTO> carItemViewDTOList = shoppingCarService.listShoppingCarDetails(userId);

        // Assert
        assertNotNull(carItemViewDTOList);
        assertFalse(carItemViewDTOList.isEmpty());
        assertEquals(1, carItemViewDTOList.size());
    }
}
