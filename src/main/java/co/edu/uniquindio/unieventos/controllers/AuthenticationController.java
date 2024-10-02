package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.accountdtos.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.accountdtos.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.accountdtos.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.accountdtos.LoginDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.services.interfaces.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {


    //Aca van servicios publicos mas relacionados a la gestion de cuentas
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<MessageDTO<TokenDTO>> iniciarSesion(@Valid @RequestBody LoginDTO
                                                                      loginDTO) throws Exception{
        TokenDTO token = accountService.login(loginDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, token));
    }

    @PostMapping("/create-account")
    public ResponseEntity<MessageDTO<String>> createAccount(@Valid @RequestBody CreateAccountDTO account) throws Exception {
        accountService.createAccount(account);
        return ResponseEntity.ok(new MessageDTO<>(false, "Account created successfully"));

    }


    @PutMapping("/send-recover/{email}")
    public ResponseEntity<MessageDTO<String>> sendRecoverPasswordCode(@PathVariable String email) throws Exception {
        accountService.sendRecoverPasswordCode(email);
        return ResponseEntity.ok(new MessageDTO<>(false, "Recover password code sent successfully"));
    }


    @PutMapping("/change-password")
    public ResponseEntity<MessageDTO<String>> changePasswordCode
    (@Valid @RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        accountService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Password changed successfully"));
    }

    /*
        TODO Ask if the following methods should be here (The activation account methods)
        your account needs to be validated before using the web

     */
    @PutMapping("/validate-account")
    public ResponseEntity<MessageDTO<String>>validateRegistrationCode(@Valid @RequestBody ActivateAccountDTO activateAccountDTO) throws Exception{
        accountService.validateRegistrationCode(activateAccountDTO);
        return ResponseEntity.ok(new MessageDTO<>(false, "Account validated successfully"));
    }

    @PutMapping("/resend-validation/{email}")
    public ResponseEntity<MessageDTO<String>>reassignValidationRegistrationCode(@PathVariable String email) throws Exception{
        accountService.reassignValidationRegistrationCode(email);
        return ResponseEntity.ok(new MessageDTO<>(false, "Validation code reassigned successfully"));
    }

}
