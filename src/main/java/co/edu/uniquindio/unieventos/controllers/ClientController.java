package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.AddShoppingCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.CarItemViewDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.DeleteCarDetailDTO;
import co.edu.uniquindio.unieventos.dto.shoppingcardtos.EditCarDetailDTO;
import co.edu.uniquindio.unieventos.services.interfaces.OrderService;
import co.edu.uniquindio.unieventos.services.interfaces.ShoppingCarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/client")
public class ClientController {

    /*
        This is the controller for the actions a Client account can do in the web

        It has to do with the order and the shopping car and the shopping order
     */

    private final ShoppingCarService shoppingCarService;
    private final OrderService orderService;

    @PutMapping("/shoppingcar/add-item")
    public ResponseEntity<MessageDTO<String>> addShoppingCarDetail
            (@Valid @RequestBody AddShoppingCarDetailDTO addShoppingCarDetailDTO) throws Exception{
        shoppingCarService.addShoppingCarDetail(addShoppingCarDetailDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Shopping car item added successfully"));
    }

    @PutMapping("/shoppingcar/edit-item")
    public ResponseEntity<MessageDTO<String>> editCarDetail (@Valid @RequestBody EditCarDetailDTO editCarDetailDTO) throws Exception{
        shoppingCarService.editCarDetail(editCarDetailDTO);
        return ResponseEntity.ok(new MessageDTO<>(true, "Car item edited successfully"));
    }

    @DeleteMapping("/shoppingcar/delete-item")
    public ResponseEntity<MessageDTO<String>> deleteShoppingCarDetail(@Valid @RequestBody DeleteCarDetailDTO deleteCarDetailDTO) throws Exception{
        shoppingCarService.deleteShoppingCarDetail(deleteCarDetailDTO);
        return ResponseEntity.ok(new MessageDTO<>(true, "Shopping car item deleted successfully"));
    }

    @PostMapping("/shoppingcar/create/{idUser}")
    public ResponseEntity<MessageDTO<String>> createShoppingCar(@PathVariable String idUser) throws Exception{
        shoppingCarService.createShoppingCar(idUser);
        return ResponseEntity.ok(new MessageDTO<>(true, "Shopping car created successfully"));
    }

    //TODO Ask if the carrito obtener controller is for the list of the items or to get the entity of Shopping car
    //I'm gonna do it as it's for the list of the items
    @GetMapping("shoppingcar/get-items/{idUser}")
    public ResponseEntity<MessageDTO<List<CarItemViewDTO>>> listShoppingCarDetails(@PathVariable String idUser) throws Exception{
        List<CarItemViewDTO> carItems = shoppingCarService.listShoppingCarDetails(idUser);
        return ResponseEntity.ok(new MessageDTO<>(false, carItems));
    }

    //TODO The controller methods for the order








}
