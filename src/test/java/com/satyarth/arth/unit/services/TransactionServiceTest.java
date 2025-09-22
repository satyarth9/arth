package com.satyarth.arth.unit.services;

import com.satyarth.arth.constants.ServiceConstants;
import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.exceptions.OperationTypeNotFoundException;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.models.OperationType;
import com.satyarth.arth.models.Transaction;
import com.satyarth.arth.repo.AccountRepo;
import com.satyarth.arth.repo.OperationTypeRepo;
import com.satyarth.arth.repo.TransactionRepo;
import com.satyarth.arth.services.TransactionService;
import com.satyarth.arth.utils.TransactionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private OperationTypeRepo operationTypeRepo;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionCreationDto transactionCreationDto;
    private Account account;
    private OperationType operationType;
    private Transaction transaction;
    private TransactionResponseDto transactionResponseDto;

    @BeforeEach
    void setUp() {
        transactionCreationDto = new TransactionCreationDto();
        transactionCreationDto.setAccountId(1L);
        transactionCreationDto.setOperationTypeId(4L);
        transactionCreationDto.setAmount(new BigDecimal("1729"));

        account = new Account();
        account.setId(1L);
        account.setDocumentNumber("0123456789");

        operationType = new OperationType();
        operationType.setId(4L);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setAmount(new BigDecimal("1729"));
        transaction.setEventDateTime(System.currentTimeMillis());

        transactionResponseDto = new TransactionResponseDto();
        transactionResponseDto.setId(1L);
        transactionResponseDto.setAccountId(1L);
        transactionResponseDto.setOperationTypeId(4L);
        transactionResponseDto.setAmount(new BigDecimal("1729"));
    }

    @Test
    void createTxn_ValidInput_ReturnsTransactionResponseDto() {

        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepo.findById(4L)).thenReturn(Optional.of(operationType));
        when(transactionRepo.save(any(Transaction.class))).thenReturn(transaction);

        try (MockedStatic<TransactionUtils> utilsMock = mockStatic(TransactionUtils.class)) {
            utilsMock.when(() -> TransactionUtils.toTransaction(
                            eq(transactionCreationDto), eq(account), eq(operationType)))
                    .thenReturn(transaction);
            utilsMock.when(() -> TransactionUtils.toDto(eq(transaction)))
                    .thenReturn(transactionResponseDto);

            TransactionResponseDto result = transactionService.createTxn(transactionCreationDto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(1L, result.getAccountId());
            assertEquals(4L, result.getOperationTypeId());
            assertEquals(new BigDecimal("1729"), result.getAmount());

            verify(accountRepo).findById(1L);
            verify(operationTypeRepo).findById(4L);
            verify(transactionRepo).save(any(Transaction.class));
            utilsMock.verify(() -> TransactionUtils.toTransaction(
                    eq(transactionCreationDto), eq(account), eq(operationType)));
            utilsMock.verify(() -> TransactionUtils.toDto(eq(transaction)));
        }
    }

    @Test
    void createTxn_AccountNotFound_ThrowsAccountNotFoundException() {
        when(accountRepo.findById(1L)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> transactionService.createTxn(transactionCreationDto)
        );

        assertEquals(ServiceConstants.AccountService.NOT_FOUND, exception.getMessage());
        verify(accountRepo).findById(1L);
        verify(operationTypeRepo, never()).findById(any());
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void createTxn_OperationTypeNotFound_ThrowsOperationTypeNotFoundException() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepo.findById(4L)).thenReturn(Optional.empty());

        OperationTypeNotFoundException exception = assertThrows(
                OperationTypeNotFoundException.class,
                () -> transactionService.createTxn(transactionCreationDto)
        );

        assertEquals(ServiceConstants.OperationType.NOT_FOUND, exception.getMessage());
        verify(accountRepo).findById(1L);
        verify(operationTypeRepo).findById(4L);
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void createTxn_AccountRepoThrowsException_PropagatesException() {
        when(accountRepo.findById(1L)).thenThrow(new RuntimeException("Database exception"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.createTxn(transactionCreationDto)
        );

        assertEquals("Database exception", exception.getMessage());
        verify(accountRepo).findById(1L);
        verify(operationTypeRepo, never()).findById(any());
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void createTxn_OperationTypeRepoThrowsException_PropagatesException() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));
        when(operationTypeRepo.findById(4L)).thenThrow(new RuntimeException("Database timeout"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.createTxn(transactionCreationDto)
        );

        assertEquals("Database timeout", exception.getMessage());
        verify(accountRepo).findById(1L);
        verify(operationTypeRepo).findById(4L);
        verify(transactionRepo, never()).save(any());
    }

}
