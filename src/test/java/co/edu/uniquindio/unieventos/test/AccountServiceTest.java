package co.edu.uniquindio.unieventos.test;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.vo.User;
import co.edu.uniquindio.unieventos.model.vo.ValidationCode;
import co.edu.uniquindio.unieventos.repositories.AccountRepo;
import co.edu.uniquindio.unieventos.services.implementations.AccountServiceImp;
import co.edu.uniquindio.unieventos.services.interfaces.AccountService;
import co.edu.uniquindio.unieventos.services.interfaces.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private EmailService emailService;

    String userId = "66fb5d99f57022796cdaf218";

    @BeforeEach
    public void setUp() {
        // Limpiar o inicializar el repositorio antes de cada prueba
        //accountRepo.deleteAll();  // Si es necesario
    }

    @Test
    public void testCreateAccount() throws Exception {
        // Crear el DTO para la cuenta
        CreateAccountDTO createAccountDTO = new CreateAccountDTO(
                "123131313",
                "Pepe",
                "3147830068",
                "Mercedes del Norte No1",
                "miraortega2020@gmail.com",
                "mira0515"
        );

        // Ejecutar el método de creación de la cuenta
        String dni = accountService.createAccount(createAccountDTO);

        // Verificar que se haya retornado un DNI no nulo
        assertNotNull(dni, "El DNI devuelto no debería ser nulo");

        // Buscar la cuenta recién creada en el repositorio y verificar que exista
        Optional<Account> createdAccount = accountRepo.findAccountByDni(dni);
        assertTrue(createdAccount.isPresent(), "La cuenta debería existir en la base de datos");

        Account account = createdAccount.get();

        // Verificar que los valores almacenados coincidan con los datos proporcionados
        assertEquals(createAccountDTO.email(), account.getEmail(), "El correo electrónico no coincide");
        assertEquals(createAccountDTO.dni(), account.getUser().getDni(), "El DNI no coincide");
        assertEquals(createAccountDTO.name(), account.getUser().getName(), "El nombre no coincide");
        assertEquals(createAccountDTO.phoneNumber(), account.getUser().getPhoneNumber(), "El teléfono no coincide");
    }


    @Test
    public void testUpdateAccount() throws Exception {
        // Crear una cuenta de prueba en la base de datos


        // Crear el DTO para la actualización de la cuenta
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO(
                userId,
                "Angel",
                "32225035863",
                "1234 Main St",
                "00000");

        // Ejecutar el método de actualización de la cuenta
        String result = accountService.updateAccount(updateAccountDTO);

        // Verificar que se haya retornado un DNI no nulo
        assertNotNull(result, "El DNI devuelto no debería ser nulo");

        // Buscar la cuenta actualizada en el repositorio
        Optional<Account> updateAccount = accountRepo.findAccountByDni(result);
        //Assertions.assertTrue(updateAccount.isPresent(), "La cuenta debería existir en la base de datos");

        Account updatedAccount = accountRepo.findAccountById(updateAccountDTO.id()).get();

        // Verificar que los valores actualizados coincidan con los datos proporcionados
        assertEquals(updateAccountDTO.password(), updatedAccount.getPassword(), "La contraseña no coincide");
        assertEquals(updateAccountDTO.address(), updatedAccount.getUser().getAddress(), "La dirección no coincide");
        assertEquals(updateAccountDTO.name(), updatedAccount.getUser().getName(), "El nombre no coincide");
        assertEquals(updateAccountDTO.phoneNumber(), updatedAccount.getUser().getPhoneNumber(), "El teléfono no coincide");
    }


    @Test
    public void testDeleteAccount() throws Exception {
        // Crear una cuenta de prueba en la base de datos
        Account account = new Account();
        account.setId("66d082d1f1f27b1e5b8e1339");
        account.setStatus(AccountStatus.ACTIVE);
        accountRepo.save(account);

        // Ejecutar el método de eliminación de la cuenta
       String result = accountService.deleteAccount("66d082d1f1f27b1e5b8e1339");

        // Verificar que el resultado sea correcto
        assertEquals("Account deleted successfully", result);

        // Verificar que el estado de la cuenta se haya actualizado a eliminado
        Optional<Account> deletedAccount = accountRepo.findAccountById("66d082d1f1f27b1e5b8e1339");
        assertTrue(deletedAccount.isPresent(), "La cuenta debería seguir existiendo en la base de datos");
        assertEquals(AccountStatus.DELETED, deletedAccount.get().getStatus(), "El estado de la cuenta no se actualizó correctamente");
    }

    @Test
    public void testLogin() throws Exception {

        Account account = accountRepo.findAccountById(userId).get();

        // Ejecutar el método de login
        LoginDTO loginDTO = new LoginDTO(account.getEmail(), account.getPassword());
        TokenDTO tokenDTO = accountService.login(loginDTO);

        // No hay necesidad de hacer más asserts, ya que el método arrojaría una excepción si fallara
        assertNotNull(tokenDTO.token(), "El token devuelto no debería ser nulo");

    }

    @Test
    public void testGetInfoAccount() throws Exception {
        // Arrange


        // Act
        AccountInfoDTO accountInfo = accountService.getInfoAccount(userId);

        // Assert
        //TODO Modificar valores de comparacion
        assertNotNull(accountInfo);
        assertEquals(userId, accountInfo.id());
        assertEquals("1091884520", accountInfo.dni());
        assertEquals("Santiago Quintero", accountInfo.name());
        assertEquals("3147830068", accountInfo.phoneNumber());
        assertEquals("3147830068", accountInfo.address());
        assertEquals("santiquinterouribe0412@gmail.com", accountInfo.email());
    }

    @Test
    public void testSendRecoverPasswordCode() throws Exception {
        // Arrange

        Account account = accountRepo.findAccountById(userId).get();

        // Act
        String result = accountService.sendRecoverPasswordCode(account.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals("A validation code has been sent to your email, check your email, it lasts 15 minutes.", result);

        Account accountFromRepo = accountRepo.findAccountByEmail(account.getEmail()).get();
        assertNotNull(accountFromRepo.getPasswordValidationCode());
    }

    @Test
    public void testChangePassword() throws Exception {
        // Arrange
        String email = "test@example.com";
        String verificationCode = "ABC123";
        String newPassword = "newPassword";

        Account account = new Account();
        account.setEmail(email);

        ValidationCode validationCode = new ValidationCode(LocalDateTime.now().minusMinutes(20), verificationCode);
        account.setPasswordValidationCode(validationCode);

        accountRepo.save(account); // Guardar la cuenta en el repo

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(email, verificationCode, newPassword);
        System.out.println(verificationCode+" es diferente a "+validationCode.getCode());
        // Act
        String result = accountService.changePassword(changePasswordDTO);

        // Assert
        assertEquals("The password has been changed successfully", result);

        Account accountFromRepo = accountRepo.findAccountByEmail(email).get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(newPassword, accountFromRepo.getPassword()));
        accountRepo.delete(accountFromRepo);
    }


    @Test
    public void testValidateRegistrationCode() throws Exception {
        // Arrange

        String email = "test@example.com";
        String validationCode = "ABC123";

        // Crear una cuenta con un código de validación
        Account account = new Account();
        account.setEmail(email);
        ValidationCode validationCodeObj = new ValidationCode(LocalDateTime.now().minusMinutes(20), validationCode);
        account.setRegistrationValidationCode(validationCodeObj);
        account.setStatus(AccountStatus.INACTIVE);

        accountRepo.save(account); // Guardar la cuenta en el repositorio

        ActivateAccountDTO activateAccountDTO = new ActivateAccountDTO(email, validationCode);

        // Act
        String result = accountService.validateRegistrationCode(activateAccountDTO);

        // Assert
        assertNotNull(result);
        Account accountFromRepo = accountRepo.findAccountByEmail(email).get();
        assertEquals(AccountStatus.ACTIVE, accountFromRepo.getStatus());
        accountRepo.delete(accountFromRepo);
    }

    @Test
    public void testReassignValidationRegistrationCode() throws Exception {
        // Arrange

        String email = "test@example.com";

        // Crear una cuenta con un código de validación antiguo
        Account account = new Account();
        account.setEmail(email);
        account.setStatus(AccountStatus.INACTIVE);
        account.setRegistrationValidationCode(new ValidationCode(LocalDateTime.now(), "OLD123"));

        accountRepo.save(account); // Guardar la cuenta en el repositorio

        // Act
        String result = accountService.reassignValidationRegistrationCode(email);

        // Assert
        assertNotNull(result);
        Account accountFromRepo = accountRepo.findAccountByEmail(email).get();

        // Verificar que el código de validación ha sido reasignado y es diferente al anterior
        assertNotNull(accountFromRepo.getRegistrationValidationCode());
        assertNotEquals("OLD123", accountFromRepo.getRegistrationValidationCode().getCode());

        // También verificar que el nuevo código de validación tenga una fecha de creación reciente
        assertTrue(accountFromRepo.getRegistrationValidationCode().getCreationDate().isAfter(LocalDateTime.now().minusMinutes(1)));
        accountRepo.delete(accountFromRepo);
    }







}
