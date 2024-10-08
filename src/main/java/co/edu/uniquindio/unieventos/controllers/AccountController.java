package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.services.interfaces.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")

//TODO Mirar bien como funcionan estas cosas con el Swagger
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;


    @PutMapping("/update-account")
    public ResponseEntity<MessageDTO<String>> updateAccount(@Valid @RequestBody UpdateAccountDTO account) throws Exception {
        String accountId=accountService.updateAccount(account);
        return ResponseEntity.ok(new MessageDTO<>(false, accountId));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageDTO<String>> deleteAccount(@PathVariable String id) throws Exception {
        String accountId= accountService.deleteAccount(id);
        return ResponseEntity.ok(new MessageDTO<>(false, accountId));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MessageDTO<AccountInfoDTO>> getInfoAccount(@PathVariable String id) throws Exception {
        AccountInfoDTO info = accountService.getInfoAccount(id);
        return ResponseEntity.ok(new MessageDTO<>(false, info));
    }



}
