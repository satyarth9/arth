package com.satyarth.arth.integration;

import com.satyarth.arth.dto.TransactionCreationDto;
import com.satyarth.arth.dto.TransactionResponseDto;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.models.OperationType;
import com.satyarth.arth.models.Transaction;
import com.satyarth.arth.repo.AccountRepo;
import com.satyarth.arth.repo.OperationTypeRepo;
import com.satyarth.arth.repo.TransactionRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransactionControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private OperationTypeRepo operationTypeRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    private Long accountId;

    @BeforeEach
    void seedData(){
        OperationType op1 = new OperationType(4L, "PAYMENT", true);
        OperationType op2 = new OperationType(3L, "WITHDRAWAL", false);
        operationTypeRepo.saveAll(List.of(op1, op2));

        Account account = new Account();
        account.setDocumentNumber("1234567899");
        account = accountRepo.save(account);
        accountId = account.getId();
    }

    @AfterEach
    void cleanUp(){
        transactionRepo.deleteAll();
        accountRepo.deleteAll();
        operationTypeRepo.deleteAll();
    }

    @Test
    void shouldCreateCreditTransaction(){
        TransactionCreationDto creationDto = new TransactionCreationDto();
        creationDto.setAmount(BigDecimal.valueOf(100L));
        creationDto.setOperationTypeId(4L);
        creationDto.setAccountId(accountId);
        ResponseEntity<TransactionResponseDto> response = testRestTemplate
                .postForEntity("/transactions", creationDto, TransactionResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(100, response.getBody().getAmount().intValue());
    }

    @Test
    void shouldCreateDebitTransaction(){
        TransactionCreationDto creationDto = new TransactionCreationDto();
        creationDto.setAmount(BigDecimal.valueOf(50L));
        creationDto.setOperationTypeId(3L);
        creationDto.setAccountId(accountId);
        ResponseEntity<TransactionResponseDto> response = testRestTemplate
                .postForEntity("/transactions", creationDto, TransactionResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(-50, response.getBody().getAmount().intValue());
    }

    @Test
    void shouldProduceBadRequest_InvalidOperationType(){
        TransactionCreationDto creationDto = new TransactionCreationDto();
        creationDto.setAmount(BigDecimal.valueOf(50L));
        creationDto.setOperationTypeId(5L);
        creationDto.setAccountId(accountId);
        ResponseEntity<TransactionResponseDto> response = testRestTemplate
                .postForEntity("/transactions", creationDto, TransactionResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldProduceNotFound_NonExistingAccountId(){
        TransactionCreationDto creationDto = new TransactionCreationDto();
        creationDto.setAmount(BigDecimal.valueOf(50L));
        creationDto.setOperationTypeId(3L);
        creationDto.setAccountId(accountId + 2);
        ResponseEntity<TransactionResponseDto> response = testRestTemplate
                .postForEntity("/transactions", creationDto, TransactionResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
