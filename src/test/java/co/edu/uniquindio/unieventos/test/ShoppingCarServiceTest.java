package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.AddShoppingCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.DeleteCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.CarItemViewDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.EditCarDetailDTO;
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

    private String userId = "6706047ac127c9d5e7e16cc0";
    private String eventId = "6706047ac127c9d5e7e16cc6";

    @BeforeEach
    void setUp() {

    }

/*
    @Test
    public void testCreateShoppingCar() throws Exception {
        String userId = "66faf8347e0c1e7206761d25"; // ID de usuario válido
        ShoppingCar existingShoppingCar = shoppingCarRepo.findByUserId(new ObjectId(userId)).orElse(null);

        if (existingShoppingCar != null) {
            shoppingCarRepo.delete(existingShoppingCar);
        }

        String carId = shoppingCarService.createShoppingCar(userId);

        assertNotNull(carId, "El ID del carrito no debería ser nulo");

        Optional<ShoppingCar> createdShoppingCar = shoppingCarRepo.findById(carId);
        Assertions.assertTrue(createdShoppingCar.isPresent(), "El carrito debería existir en la base de datos");

        ShoppingCar shoppingCar = createdShoppingCar.get();
        assertEquals(userId, shoppingCar.getUserId().toString(), "El ID del usuario debería coincidir");
        assertTrue(shoppingCar.getItems().isEmpty(), "El carrito debería estar vacío inicialmente");
        assertNotNull(shoppingCar.getDate(), "La fecha de creación del carrito no debería ser nula");
    }
*/

    @Test
    public void testAddShoppingCarDetail() throws Exception {
        Event event = eventRepo.findById(eventId).get();
        Location location = event.findLocationByName("General");
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
        String userId = "6707783f1c261697d9b3f989"; // ID del usuario
        String eventId = "6706047ac127c9d5e7e16cc8"; // ID del evento
        DeleteCarDetailDTO deleteCarDetailDTO = new DeleteCarDetailDTO(userId, eventId, "Pasarela");
        List<CarDetail> carDetails = new ArrayList<>();
        CarDetail carDetail = new CarDetail();
        carDetail.setIdEvent(new ObjectId(eventId));
        carDetail.setLocationName("Pasarela");
        carDetails.add(carDetail);

        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.setItems(carDetails);
        shoppingCarRepo.save(shoppingCar);
        shoppingCarService.deleteShoppingCarDetail(deleteCarDetailDTO);

        ShoppingCar updatedShoppingCar = shoppingCarRepo.findById("6706114a051852d809818d4d").orElse(null);
        assertNotNull(updatedShoppingCar, "El carrito de compras debería existir después de la eliminación");

        assertFalse(updatedShoppingCar.getItems().stream()
                        .anyMatch(item -> item.getLocationName().equals("Pasarela")),
                "El detalle del carrito con 'Pasarela' debería haber sido eliminado");


    }

    @Test
    public void testListShoppingCarDetails() throws Exception {
        ShoppingCar shoppingCar = new ShoppingCar();
        List<CarDetail> carDetails = new ArrayList<>();
        CarDetail carDetail = new CarDetail();
        carDetail.setIdEvent(new ObjectId(eventId));
        carDetail.setLocationName("Locación 1");
        carDetails.add(carDetail);
        shoppingCar.setItems(carDetails);
        shoppingCarRepo.save(shoppingCar);

        Event event = new Event();
        event.setName("Evento 1");
        event.setType(EventType.CONCERT);
        eventRepo.save(event);

        Location location = new Location();
        location.setPrice(100.0F);
        event.getLocations().add(location);

        List<CarItemViewDTO> carItemViewDTOList = shoppingCarService.listShoppingCarDetails(userId);

        assertNotNull(carItemViewDTOList);
        assertFalse(carItemViewDTOList.isEmpty());
        assertEquals(1, carItemViewDTOList.size());
    }

    //////////////////////////////////////////////////////////
    @Test
    public void testEditCarDetail() throws Exception {
        try {
            EditCarDetailDTO editCarDetailDTO=new EditCarDetailDTO(
                    "6707783f1c261697d9b3f989",
                    "6706047ac127c9d5e7e16cc8",
                    "NewLocation",
                    0
            );
            shoppingCarService.editCarDetail(editCarDetailDTO);
            assertTrue(true);
        } catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
