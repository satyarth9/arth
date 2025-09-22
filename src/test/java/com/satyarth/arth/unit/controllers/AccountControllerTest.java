package com.satyarth.arth.unit.controllers;

import com.satyarth.arth.controllers.AccountController;
import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    void getById_ValidInput_ReturnsAccountResponse() {

        when(accountService.getAccount(1L))
                .thenReturn(accountResponseDto);

        ResponseEntity<AccountResponseDto> result = accountController.getById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(accountService).getAccount(anyLong());
    }

}
