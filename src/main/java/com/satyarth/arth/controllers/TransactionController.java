package com.satyarth.arth.controllers;

import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.exceptions.OperationTypeNotFoundException;
import com.satyarth.arth.services.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("")
    public ResponseEntity<TransactionResponseDto> createTxn(@Valid @RequestBody TransactionCreationDto transactionCreationDto){
        try {
            TransactionResponseDto responseDto = transactionService.createTxn(transactionCreationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        } catch (AccountNotFoundException | OperationTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException | IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
