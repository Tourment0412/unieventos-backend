package co.edu.uniquindio.unieventos.dto.accountdtos;

public record AccountInfoDTO(
        //This id is gonna be a token in the future
        String id,
        String dni,
        String name,
        String phoneNumber,
        String address,
        String email
) {
}
