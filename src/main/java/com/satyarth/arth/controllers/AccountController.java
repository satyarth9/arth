package com.satyarth.arth.controllers;

import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.services.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountCreationDto accountCreationDto){
        try {
            AccountResponseDto responseDto = accountService.create(accountCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getById(@PathVariable Long accountId){
        try {
            AccountResponseDto responseDto = accountService.getAccount(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (AccountNotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
