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
        AccountResponseDto responseDto = accountService.create(accountCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getById(@PathVariable Long accountId){
        AccountResponseDto responseDto = accountService.getAccount(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
