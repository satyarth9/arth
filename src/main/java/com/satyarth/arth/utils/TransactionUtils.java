package com.satyarth.arth.utils;

import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.models.OperationType;
import com.satyarth.arth.models.Transaction;

public class TransactionUtils {

    public static TransactionResponseDto toDto(Transaction transaction){
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setAccountId(transaction.getAccount().getId());
        dto.setOperationTypeId(transaction.getOperationType().getId());
        dto.setEventDateTime(transaction.getEventDateTime());
        return dto;
    }

    public static Transaction toTransaction(TransactionCreationDto creationDto,
                                            Account account, OperationType operationType){
        Transaction txn =  new Transaction();
        txn.setAccount(account);
        txn.setOperationType(operationType);
        txn.setAmount(operationType.getCredit() ? creationDto.getAmount() :
                creationDto.getAmount().negate());
        txn.setEventDateTime(System.currentTimeMillis());
        return txn;
    }
}
