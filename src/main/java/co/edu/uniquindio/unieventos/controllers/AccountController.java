package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.accountdtos.*;
import co.edu.uniquindio.unieventos.dto.jwtdtos.MessageDTO;
import co.edu.uniquindio.unieventos.dto.jwtdtos.TokenDTO;
import co.edu.uniquindio.unieventos.services.interfaces.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;


    @PutMapping("/update-account")
    public ResponseEntity<MessageDTO<String>> updateAccount(@Valid @RequestBody UpdateAccountDTO account) throws Exception {
        accountService.updateAccount(account);
        return ResponseEntity.ok(new MessageDTO<>(false, "Account updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageDTO<String>> deleteAccount(@PathVariable String id) throws Exception {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(new MessageDTO<>(false, "Account deleted successfully"));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<MessageDTO<AccountInfoDTO>> getInfoAccount(@PathVariable String id) throws Exception {
        AccountInfoDTO info = accountService.getInfoAccount(id);
        return ResponseEntity.ok(new MessageDTO<>(false, info));
    }



}
