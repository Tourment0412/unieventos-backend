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

    String userId = "6706047ac127c9d5e7e16cbf";

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
                "juanmanuel200413@gmail.com",
                "12345678"
        );

        String dni = accountService.createAccount(createAccountDTO);

        assertNotNull(dni, "El DNI devuelto no debería ser nulo");

        Optional<Account> createdAccount = accountRepo.findById(dni);
        assertTrue(createdAccount.isPresent(), "La cuenta debería existir en la base de datos");

        Account account = createdAccount.get();

        assertEquals(createAccountDTO.email(), account.getEmail(), "El correo electrónico no coincide");
        assertEquals(createAccountDTO.dni(), account.getUser().getDni(), "El DNI no coincide");
        assertEquals(createAccountDTO.name(), account.getUser().getName(), "El nombre no coincide");
        assertEquals(createAccountDTO.phoneNumber(), account.getUser().getPhoneNumber(), "El teléfono no coincide");
    }


    @Test
    public void testUpdateAccount() throws Exception {
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO(
                userId,
                "Angel",
                "32225035863",
                "1234 Main St",
                "00200");

        String result = accountService.updateAccount(updateAccountDTO);

        assertNotNull(result, "El DNI devuelto no debería ser nulo");

        Optional<Account> updateAccount = accountRepo.findAccountByDni(result);
        Assertions.assertTrue(updateAccount.isEmpty(), "La cuenta debería existir en la base de datos");

        Account updatedAccount = accountRepo.findAccountById(updateAccountDTO.id()).get(); //Es necesario validar si el opcional está vacio

        assertEquals(updateAccountDTO.address(), updatedAccount.getUser().getAddress(), "La dirección no coincide");
        assertEquals(updateAccountDTO.name(), updatedAccount.getUser().getName(), "El nombre no coincide");
        assertEquals(updateAccountDTO.phoneNumber(), updatedAccount.getUser().getPhoneNumber(), "El teléfono no coincide");
    }


    @Test
    public void testDeleteAccount() throws Exception {
        Account account = new Account();
        account.setId("66d082d1f1f27b1e5b8e1339");
        account.setStatus(AccountStatus.ACTIVE);
        accountRepo.save(account);

       String result = accountService.deleteAccount("66d082d1f1f27b1e5b8e1339");

        assertEquals("66d082d1f1f27b1e5b8e1339", result);

        Optional<Account> deletedAccount = accountRepo.findAccountById("66d082d1f1f27b1e5b8e1339");
        assertTrue(deletedAccount.isPresent(), "La cuenta no debería seguir existiendo en la base de datos");
        assertEquals(AccountStatus.DELETED, deletedAccount.get().getStatus(), "El estado de la cuenta no se actualizó correctamente");
    }

    //Tengo problemas al ejecutar el login ya que no se puede comparar corectamente las contraseñas de la prueba con la de la base de datos
    @Test
    public void testLogin() throws Exception {
        Account account = accountRepo.findAccountById(userId).get();

        LoginDTO loginDTO = new LoginDTO(account.getEmail(), "12345678");
        TokenDTO tokenDTO = accountService.login(loginDTO);

        assertNotNull(tokenDTO.token(), "El token devuelto no debería ser nulo");

    }

    @Test
    public void testGetInfoAccount() throws Exception {
        String id="6706047ac127c9d5e7e16cc0";
        AccountInfoDTO accountInfo = accountService.getInfoAccount(id);

        assertNotNull(accountInfo);
        assertEquals(id, accountInfo.id());
        assertEquals("2222222222", accountInfo.dni());
        assertEquals("Maria Lopez", accountInfo.name());
        assertEquals("0987654321", accountInfo.phoneNumber());
        assertEquals("Calle 2", accountInfo.address());
        assertEquals("santiquinterouribe0412@gmail.com", accountInfo.email());
    }

    @Test
    public void testSendRecoverPasswordCode() throws Exception {
        Account account = accountRepo.findAccountById(userId).get();

        String result = accountService.sendRecoverPasswordCode(account.getEmail());

        assertNotNull(result);
        assertEquals(userId, result);

        Account accountFromRepo = accountRepo.findAccountByEmail(account.getEmail()).get();
        assertNotNull(accountFromRepo.getPasswordValidationCode());
    }

    @Test
    public void testChangePassword() throws Exception {
        // Arrange
        String email = "example@gmail.com";
        String verificationCode = "ABC123";
        String newPassword = "newPassword";

        Account account = new Account();
        account.setId("67072d61d18f5db00879b7c8");
        account.setEmail(email);

        ValidationCode validationCode = new ValidationCode(LocalDateTime.now(), verificationCode);
        account.setPasswordValidationCode(validationCode);

        accountRepo.save(account); // Guardar la cuenta en el repo

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(email, verificationCode, newPassword);
        System.out.println(verificationCode+" es diferente a "+validationCode.getCode());

        String result = accountService.changePassword(changePasswordDTO);

        assertEquals("67072d61d18f5db00879b7c8", result);

        Account accountFromRepo = accountRepo.findAccountByEmail(email).get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(newPassword, accountFromRepo.getPassword()));
        accountRepo.delete(accountFromRepo);
    }


    @Test
    public void testValidateRegistrationCode() throws Exception {
        String email = "test2@example.com";
        String validationCode = "ABC123";

        Account account = new Account();
        account.setEmail(email);
        ValidationCode validationCodeObj = new ValidationCode(LocalDateTime.now(), validationCode);
        account.setRegistrationValidationCode(validationCodeObj);
        account.setStatus(AccountStatus.INACTIVE);

        accountRepo.save(account); // Guardar la cuenta en el repositorio

        ActivateAccountDTO activateAccountDTO = new ActivateAccountDTO(email, validationCode);

        String result = accountService.validateRegistrationCode(activateAccountDTO);

        assertNotNull(result);
        Account accountFromRepo = accountRepo.findAccountByEmail(email).get();
        assertEquals(AccountStatus.ACTIVE, accountFromRepo.getStatus());
        accountRepo.delete(accountFromRepo);
    }

    @Test
    public void testReassignValidationRegistrationCode() throws Exception {
        String email = "test3@example.com";

        Account account = new Account();
        account.setEmail(email);
        account.setStatus(AccountStatus.INACTIVE);
        account.setRegistrationValidationCode(new ValidationCode(LocalDateTime.now(), "OLD123"));

        accountRepo.save(account); // Guardar la cuenta en el repositorio

        String result = accountService.reassignValidationRegistrationCode(email);

        assertNotNull(result);
        Account accountFromRepo = accountRepo.findAccountByEmail(email).get();

        assertNotNull(accountFromRepo.getRegistrationValidationCode());
        assertNotEquals("OLD123", accountFromRepo.getRegistrationValidationCode().getCode());

        assertTrue(accountFromRepo.getRegistrationValidationCode().getCreationDate().isAfter(LocalDateTime.now().minusMinutes(1)));
        accountRepo.delete(accountFromRepo);
    }







}
