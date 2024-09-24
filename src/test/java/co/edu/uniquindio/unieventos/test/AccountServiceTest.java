package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.dto.accountdtos.CreateAccountDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.repositories.AccountRepo;
import co.edu.uniquindio.unieventos.services.implementations.AccountServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountServiceImp accountServiceImp;

    @Autowired
    private AccountRepo accountRepo;

    @Test
    public void testCreateAccount() throws Exception {
        // Crear el DTO para la cuenta
        CreateAccountDTO createAccountDTO = new CreateAccountDTO(
                "1091884520",
                "Santiago Quintero",
                "3147830068",
                "Mercedes del Norte No1",
                "santiquinterouribe0412@gmail.com",
                "santi0909"
        );

        // Ejecutar el método de creación de la cuenta
        String dni = accountServiceImp.createAccount(createAccountDTO);


        // Verificar que se haya retornado un DNI no nulo
        Assertions.assertNotNull(dni, "El DNI devuelto no debería ser nulo");

        // Buscar la cuenta recién creada en el repositorio y verificar que exista

        Optional<Account> createdAccount = accountRepo.findAccountByDni(dni);

        if(createdAccount.isEmpty()){
            throw new Exception("El DNI no existe");
        }
        Account account = createdAccount.get();
        Assertions.assertNotNull(account, "La cuenta debería existir en la base de datos");

        // Verificar que los valores almacenados coincidan con los datos proporcionados
        Assertions.assertEquals(createAccountDTO.email(), account.getEmail(), "El correo electrónico no coincide");
        Assertions.assertEquals(createAccountDTO.dni(), account.getUser().getDni(), "El DNI no coincide");
        Assertions.assertEquals(createAccountDTO.name(), account.getUser().getName(), "El nombre no coincide");
        Assertions.assertEquals(createAccountDTO.phoneNumber(), account.getUser().getPhoneNumber(), "El teléfono no coincide");

    }
}
