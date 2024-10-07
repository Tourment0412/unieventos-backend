package co.edu.uniquindio.unieventos.dto.orderdtos;

import org.bson.types.ObjectId;

public record OrderDetailDTO(
        String eventId,
        float price,
        String locationName,
        int quantity

) {
}
