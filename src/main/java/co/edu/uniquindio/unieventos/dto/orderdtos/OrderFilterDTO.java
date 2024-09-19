package co.edu.uniquindio.unieventos.dto.orderdtos;

import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;

import java.time.LocalDateTime;
import java.util.List;

public record OrderFilterDTO(
       
        LocalDateTime orderDate

) {

}
