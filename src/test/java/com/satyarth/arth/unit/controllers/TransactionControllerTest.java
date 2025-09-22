package com.satyarth.arth.unit.controllers;

import com.satyarth.arth.constants.ServiceConstants;
import com.satyarth.arth.controllers.TransactionController;
import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.exceptions.OperationTypeNotFoundException;
import com.satyarth.arth.services.TransactionService;
import jakarta.xml.bind.ValidationException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionCreationDto transactionCreationDto;
    private TransactionResponseDto transactionResponseDto;


    @BeforeEach
    void setUp() {
        transactionCreationDto = new TransactionCreationDto();
        transactionCreationDto.setAccountId(1L);
        transactionCreationDto.setOperationTypeId(4L);
        transactionCreationDto.setAmount(new BigDecimal("1331"));

        transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(100L);
        transactionResponseDto.setAccountId(1L);
        transactionResponseDto.setOperationTypeId(4L);
        transactionResponseDto.setAmount(new BigDecimal("1331"));
        transactionResponseDto.setEventDateTime(System.currentTimeMillis());
    }

    @Test
    void createTxn_ValidInput_ReturnsCreatedWithTransactionResponse() {

        when(transactionService.createTxn(any(TransactionCreationDto.class)))
                .thenReturn(transactionResponseDto);

        ResponseEntity<TransactionResponseDto> result = transactionController.createTxn(transactionCreationDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(100L, result.getBody().getId());
        assertEquals(1L, result.getBody().getAccountId());
        assertEquals(4L, result.getBody().getOperationTypeId());
        assertEquals(new BigDecimal("1331"), result.getBody().getAmount());

        verify(transactionService).createTxn(any(TransactionCreationDto.class));
    }

    @Test
    void createTxn_AccountNotFound_ReturnsNotFound() {
        when(transactionService.createTxn(any(TransactionCreationDto.class)))
                .thenThrow(new AccountNotFoundException(ServiceConstants.AccountService.NOT_FOUND));

        ResponseEntity<TransactionResponseDto> result = transactionController.createTxn(transactionCreationDto);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(transactionService).createTxn(any(TransactionCreationDto.class));
    }

    @Test
    void createTxn_OperationTypeNotFound_ReturnsNotFound() {
        when(transactionService.createTxn(any(TransactionCreationDto.class)))
                .thenThrow(new OperationTypeNotFoundException(ServiceConstants.OperationType.NOT_FOUND));

        ResponseEntity<TransactionResponseDto> result = transactionController.createTxn(transactionCreationDto);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());

        verify(transactionService).createTxn(any(TransactionCreationDto.class));
    }

    @Test
    void createTxn_IllegalArgument_ReturnsBadRequest() {
        when(transactionService.createTxn(any(TransactionCreationDto.class)))
                .thenThrow(new IllegalArgumentException("Illegal argument"));

        ResponseEntity<TransactionResponseDto> result = transactionController.createTxn(transactionCreationDto);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());

        verify(transactionService).createTxn(any(TransactionCreationDto.class));
    }

    @Test
    void createTxn_UnexpectedException_ReturnsInternalServerError() {
        when(transactionService.createTxn(any(TransactionCreationDto.class)))
                .thenThrow(new RuntimeException("Server runtime error"));

        ResponseEntity<TransactionResponseDto> result = transactionController.createTxn(transactionCreationDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());

        verify(transactionService).createTxn(any(TransactionCreationDto.class));
    }

}
