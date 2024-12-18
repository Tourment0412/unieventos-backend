package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.*;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.repositories.ShoppingCarRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ShoppingCarServiceImp implements ShoppingCarService {

    private final ShoppingCarRepo shoppingCarRepo;
    private final EventService eventService;

    public ShoppingCarServiceImp(ShoppingCarRepo shoppingCarRepo, EventService eventService) {
        this.shoppingCarRepo = shoppingCarRepo;
        this.eventService = eventService;
    }

    @Override
    public ShoppingCar createShoppingCar(String idUser) {
        Optional<ShoppingCar> shoppingCarReceived = shoppingCarRepo.findByUserId(new ObjectId(idUser));

        if (shoppingCarReceived.isEmpty()) {

            ShoppingCar shoppingCar = new ShoppingCar();
            shoppingCar.setUserId(new ObjectId(idUser));
            shoppingCar.setDate(LocalDateTime.now());
            shoppingCar.setItems(new ArrayList<>());

            return shoppingCarRepo.save(shoppingCar);
        } else {
            return shoppingCarReceived.get();
        }
    }


    @Override
    public void deleteShoppingCar(String idUser) throws EmptyShoppingCarException{
        ShoppingCar shoppingCar= getShoppingCar(idUser);
        shoppingCarRepo.delete(shoppingCar);
    }
    @Override
    public String addShoppingCarDetail(AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws ResourceNotFoundException, OperationNotAllowedException, InsufficientCapacityException {
        ShoppingCar shoppingCar = createShoppingCar(addShoppingCarDetailDTO.idUser());
        Event event = eventService.getEvent(addShoppingCarDetailDTO.idEvent());
        Location location = event.findLocationByName(addShoppingCarDetailDTO.locationName());

        if (!location.isCapacityAvaible(addShoppingCarDetailDTO.quantity())) {
            throw new InsufficientCapacityException("Max capacity exceeded");
        }

        CarDetail carDetail = new CarDetail();
        carDetail.setAmount(addShoppingCarDetailDTO.quantity());
        carDetail.setIdEvent(new ObjectId(addShoppingCarDetailDTO.idEvent()));
        carDetail.setLocationName(addShoppingCarDetailDTO.locationName());

        List<CarDetail> details = shoppingCar.getItems();
        details.add(carDetail);
        shoppingCar.setItems(details);
        return shoppingCarRepo.save(shoppingCar).getId();
    }

    @Override
    public String deleteShoppingCarDetail(DeleteCarDetailDTO deleteCarDetailDTO) throws EmptyShoppingCarException {
        ShoppingCar shoppingCar = getShoppingCar(deleteCarDetailDTO.idUser());
        List<CarDetail> details = shoppingCar.getItems();
        details.removeIf(e -> e.getIdEvent().toString().equals(deleteCarDetailDTO.idEvent()) &&
                e.getLocationName().equals(deleteCarDetailDTO.locationName()));
        shoppingCar.setItems(details);
        return shoppingCarRepo.save(shoppingCar).getId();
    }

    @Override
    public List<CarItemViewDTO> listShoppingCarDetails(String userId) throws EmptyShoppingCarException {
        ShoppingCar shoppingCar = createShoppingCar(userId);
        List<CarDetail> shoppingCarDetails = shoppingCar.getItems();

        return shoppingCarDetails.stream()
                .map(this::convertToCarItemViewDTO)
                .flatMap(Optional::stream) // Descartar valores nulos si la conversión falló
                .collect(Collectors.toList());
    }

    private Optional<CarItemViewDTO> convertToCarItemViewDTO(CarDetail itemView) {
        try {
            Event event = eventService.getEvent(itemView.getIdEvent().toString());
            Location location = event.findLocationByName(itemView.getLocationName());

            return Optional.of(new CarItemViewDTO(
                    event.getId(),
                    event.getName(),
                    itemView.getLocationName(),
                    event.getType(),
                    location.getPrice(),
                    itemView.getAmount(),
                    location.getPrice() * itemView.getAmount()
            ));

        } catch (Exception e) {
            // Registro del error para informar o para debugging
            System.err.println("Error: " + e.getMessage());
            return Optional.empty(); // Retornar vacío si hay un error en la conversión
        }
    }

    @Override
    public String editCarDetail(EditCarDetailDTO editCarDetailDTO) throws EmptyShoppingCarException{
        ShoppingCar shoppingCar = getShoppingCar(editCarDetailDTO.idUser());
        List<CarDetail> details = shoppingCar.getItems();
        details.forEach(e -> {
            try {
                Event event = eventService.getEvent(editCarDetailDTO.idEvent());
                Location location = event.findLocationByName(editCarDetailDTO.locationName());

                if (e.getIdEvent().toString().equals(editCarDetailDTO.idEvent()) &&
                        e.getLocationName().equals(editCarDetailDTO.locationName())) {
                    if (!location.isCapacityAvaible(editCarDetailDTO.amount())) {
                        throw new InsufficientCapacityException("Max capacity exceeded");
                    }else{
                        e.setAmount(editCarDetailDTO.amount());
                    }

                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });
        shoppingCar.setItems(details);
        return shoppingCarRepo.save(shoppingCar).getId();
    }

    //This method is going to be used
    @Override
    public ShoppingCar getShoppingCar(String userId) throws EmptyShoppingCarException {
        Optional<ShoppingCar> shoppingCar = shoppingCarRepo.findByUserId(new ObjectId(userId));
        if (shoppingCar.isEmpty()) {
            throw new EmptyShoppingCarException("There's no shopping car for this user");
        }
        return shoppingCar.get();
    }

    private boolean existsShoppingCar(String userId) {
        return shoppingCarRepo.findByUserId(new ObjectId(userId)).isPresent();
    }






}
