package com.satyarth.arth.unit.controllers;

import com.satyarth.arth.constants.ServiceConstants;
import com.satyarth.arth.controllers.AccountController;
import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountCreationDto accountCreationDto;
    private AccountResponseDto accountResponseDto;

    @BeforeEach
    void setUp() {
        accountCreationDto = new AccountCreationDto();
        accountCreationDto.setDocumentNumber("123456789");

        accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setDocumentNumber("123456789");
    }


    @Test
    void createAccount_ValidInput_ReturnsCreatedWithAccountResponse() {

        when(accountService.create(any(AccountCreationDto.class)))
                .thenReturn(accountResponseDto);

        ResponseEntity<AccountResponseDto> result = accountController.createAccount(accountCreationDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1L, result.getBody().getId());
        assertEquals("123456789", result.getBody().getDocumentNumber());

        verify(accountService).create(any(AccountCreationDto.class));
    }

    @Test
    void createAccount_IllegalArgument_ReturnsBadRequest() {

        when(accountService.create(any(AccountCreationDto.class)))
                .thenThrow(new IllegalArgumentException("ILLEGAL ARGUMENT"));

        ResponseEntity<AccountResponseDto> result = accountController.createAccount(accountCreationDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
        verify(accountService).create(any(AccountCreationDto.class));
    }

    @Test
    void createAccount_UnexpectedException_ReturnsInternalServerError() {

        when(accountService.create(any(AccountCreationDto.class)))
                .thenThrow(new RuntimeException("Server runtime error"));

        ResponseEntity<AccountResponseDto> result = accountController.createAccount(accountCreationDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
        verify(accountService).create(any(AccountCreationDto.class));
    }

    @Test
    void getById_ValidInput_ReturnsAccountResponse() {

        when(accountService.getAccount(1L))
                .thenReturn(accountResponseDto);

        ResponseEntity<AccountResponseDto> result = accountController.getById(1L);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(accountService).getAccount(anyLong());
    }

    @Test
    void getById_AccountNotFound_ReturnsNotFound() {

        when(accountService.getAccount(1L))
                .thenThrow(new AccountNotFoundException(ServiceConstants.AccountService.NOT_FOUND));

        ResponseEntity<AccountResponseDto> result = accountController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
        verify(accountService).getAccount(anyLong());
    }

    @Test
    void getById_IllegalArgument_ReturnsBadRequest() {

        when(accountService.getAccount(1L))
                .thenThrow(new IllegalArgumentException("ILLEGAL ARGUMENT"));

        ResponseEntity<AccountResponseDto> result = accountController.getById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
        verify(accountService).getAccount(anyLong());
    }

    @Test
    void getById_UnexpectedException_ReturnsInternalServerError() {

        when(accountService.getAccount(1L))
                .thenThrow(new RuntimeException("Server runtime error"));

        ResponseEntity<AccountResponseDto> result = accountController.getById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
        verify(accountService).getAccount(anyLong());
    }
}
