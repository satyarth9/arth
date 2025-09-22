package com.satyarth.arth.services;

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
import com.satyarth.arth.utils.TransactionUtils;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final OperationTypeRepo operationTypeRepo;

    public TransactionService(TransactionRepo transactionRepo, AccountRepo accountRepo, OperationTypeRepo operationTypeRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.operationTypeRepo = operationTypeRepo;
    }

    public TransactionResponseDto createTxn(TransactionCreationDto creationDto){
        Account account = accountRepo.findById(creationDto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(ServiceConstants.AccountService.NOT_FOUND));
        OperationType operationType = operationTypeRepo.findById(creationDto.getOperationTypeId())
                .orElseThrow(() -> new OperationTypeNotFoundException(ServiceConstants.OperationType.NOT_FOUND));
        Transaction transaction = transactionRepo.save(
                TransactionUtils.toTransaction(creationDto, account, operationType));
        return TransactionUtils.toDto(transaction);
    }
}
