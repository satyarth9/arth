package com.satyarth.arth.unit.utils;

import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.models.OperationType;
import com.satyarth.arth.models.Transaction;
import com.satyarth.arth.utils.TransactionUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionUtilsTest {

    @Test
    void toDto_ValidTransaction_ReturnsCorrectDto() {
        Account account = new Account();
        account.setId(1L);

        OperationType operationType = new OperationType();
        operationType.setId(4L);

        long transactionTime = System.currentTimeMillis();
        Transaction transaction = new Transaction();
        transaction.setId(100L);
        transaction.setAmount(new BigDecimal("1729"));
        transaction.setAccount(account);
        transaction.setOperationType(operationType);
        transaction.setEventDateTime(transactionTime);

        TransactionResponseDto result = TransactionUtils.toDto(transaction);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(new BigDecimal("1729"), result.getAmount());
        assertEquals(1L, result.getAccountId());
        assertEquals(4L, result.getOperationTypeId());
        assertEquals(transactionTime, result.getEventDateTime());
    }

    @Test
    void toTransaction_ValidInputWithCreditOperation_ReturnsCorrectTransaction() {
        TransactionCreationDto creationDto = new TransactionCreationDto();
        creationDto.setAmount(new BigDecimal("1729"));

        Account account = new Account();
        account.setId(1L);

        OperationType operationType = new OperationType();
        operationType.setId(4L); // credit type
        operationType.setCredit(true); // already present in db

        Transaction result = TransactionUtils.toTransaction(creationDto, account, operationType);

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(operationType, result.getOperationType());
        assertEquals(new BigDecimal("1729"), result.getAmount());
    }

    @Test
    void toTransaction_ValidInputWithDebitOperation_ReturnsCorrectTransaction() {
        TransactionCreationDto creationDto = new TransactionCreationDto();
        creationDto.setAmount(new BigDecimal("1729"));

        Account account = new Account();
        account.setId(1L);

        OperationType operationType = new OperationType();
        operationType.setId(3L); // debit type
        operationType.setCredit(false); // already present in db

        Transaction result = TransactionUtils.toTransaction(creationDto, account, operationType);

        assertNotNull(result);
        assertEquals(account, result.getAccount());
        assertEquals(operationType, result.getOperationType());
        assertEquals(new BigDecimal("-1729"), result.getAmount());
    }
}