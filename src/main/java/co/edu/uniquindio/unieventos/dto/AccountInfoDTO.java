package co.edu.uniquindio.unieventos.dto;

public record AccountInfoDTO(
        //This id is gonna be a token in the fure
        String id,
        String dni,
        String name,
        String phoneNumber,
        String address,
        String email
) {
}
