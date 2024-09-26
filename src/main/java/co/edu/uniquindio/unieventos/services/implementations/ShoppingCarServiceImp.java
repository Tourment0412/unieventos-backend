package co.edu.uniquindio.unieventos.services.implementations;

import co.edu.uniquindio.unieventos.dto.shoppingcardtos.*;
import co.edu.uniquindio.unieventos.model.documents.Event;
import co.edu.uniquindio.unieventos.model.documents.ShoppingCar;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.CarDetail;
import co.edu.uniquindio.unieventos.model.vo.Location;
import co.edu.uniquindio.unieventos.repositories.ShoppingCarRepo;
import co.edu.uniquindio.unieventos.services.interfaces.EventService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
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
    public String createShoppingCar(CreateShoppingCarDTO createShoppingCarDTO) throws Exception {


        if (existsShoppingCar(createShoppingCarDTO.accountId())) {
            throw new Exception("Shopping car already exists for this account");
        }
        ShoppingCar shoppingCar = new ShoppingCar();
        shoppingCar.setUserId(new ObjectId(createShoppingCarDTO.accountId()));
        shoppingCar.setDate(LocalDateTime.now());
        shoppingCar.setItems(new ArrayList<>());
        return shoppingCarRepo.save(shoppingCar).getId();
    }

    @Override
    public String addShoppingCarDetail(AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws Exception {
        ShoppingCar shoppingCar = getShoppingCar(addShoppingCarDetailDTO.idUser());
        Event event = eventService.getEvent(addShoppingCarDetailDTO.idEvent());
        Location location = event.findLocationByName(addShoppingCarDetailDTO.locationName());

        if (!location.isCapacityAvaible(addShoppingCarDetailDTO.quantity())) {
            throw new Exception("Max capacity exceeded");
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
    public void deleteShoppingCarDetail(DeleteCarDetailDTO deleteCarDetailDTO) throws Exception {
        ShoppingCar shoppingCar = getShoppingCar(deleteCarDetailDTO.idUser());
        List<CarDetail> details = shoppingCar.getItems();
        details.removeIf(e -> e.getIdEvent().toString().equals(deleteCarDetailDTO.idEvent()) &&
                e.getLocationName().equals(deleteCarDetailDTO.locationName()));
        shoppingCar.setItems(details);
        shoppingCarRepo.save(shoppingCar);
    }

    @Override
    public List<CarItemViewDTO> listShoppingCarDetails(String userId) throws Exception {
        ShoppingCar shoppingCar = getShoppingCar(userId);
        //TODO Ask if this should be an aggregation
        List<CarDetail> shoppingCarDetails = shoppingCar.getItems();
        return shoppingCarDetails.stream()
                .map(itemView -> {
                    try {
                        Event event = eventService.getEvent(itemView.getIdEvent().toString());
                        Location location = event.findLocationByName(itemView.getLocationName());
                        return new CarItemViewDTO(
                                event.getName(),
                                itemView.getLocationName(),
                                event.getType(),
                                location.getPrice(),
                                itemView.getAmount(),
                                location.getPrice() * itemView.getAmount()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

    }

    @Override
    public void editCarDetail(EditCarDetailDTO editCarDetailDTO) throws Exception {
        ShoppingCar shoppingCar = getShoppingCar(editCarDetailDTO.idUser());
        List<CarDetail> details = shoppingCar.getItems();
        details.forEach(e -> {
            try {
                Event event = eventService.getEvent(editCarDetailDTO.idEvent());
                Location location = event.findLocationByName(editCarDetailDTO.locationName());

                if (e.getIdEvent().toString().equals(editCarDetailDTO.idEvent()) &&
                        e.getLocationName().equals(editCarDetailDTO.locationName())) {
                    if (!location.isCapacityAvaible(editCarDetailDTO.amount())) {
                        throw new Exception("Max capacity exceeded");
                    }else{
                        e.setAmount(editCarDetailDTO.amount());
                    }

                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });
        shoppingCar.setItems(details);
        shoppingCarRepo.save(shoppingCar);
    }

    //This method is going to be used
    @Override
    public ShoppingCar getShoppingCar(String userId) throws Exception {
        Optional<ShoppingCar> shoppingCar = shoppingCarRepo.findByUserId(new ObjectId(userId));
        if (shoppingCar.isEmpty()) {
            throw new Exception("There's no shopping car for this user");
        }
        return shoppingCar.get();
    }

    private boolean existsShoppingCar(String userId) {
        return shoppingCarRepo.findByUserId(new ObjectId(userId)).isPresent();
    }






}
