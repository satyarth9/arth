package com.satyarth.arth.integration;

import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.repo.AccountRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public class AccountControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private AccountRepo accountRepo;

    private Long savedAccountId;

    @BeforeEach
    void seedData(){
        Account account = new Account();
        account.setDocumentNumber("1122334455");
        account = accountRepo.save(account);
        savedAccountId = account.getId();
    }

    @AfterEach
    void cleanUp(){
        accountRepo.deleteAll();
    }

    @Test
    void testConnection(){
        assertTrue(postgreSQLContainer.isCreated());
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    void shouldCreateAnAccount(){
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setDocumentNumber("1122334455");
        ResponseEntity<AccountResponseDto> response = testRestTemplate
                .postForEntity("/accounts", accountCreationDto, AccountResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().getId() > 0);
    }

    @Test
    void shouldNotCreateAnAccount_InvalidDocumentNumber(){
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setDocumentNumber("1234"); // fails validation
        ResponseEntity<AccountResponseDto> response = testRestTemplate
                .postForEntity("/accounts", accountCreationDto, AccountResponseDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldNotCreateAnAccount_BlankDocumentNumber(){
        AccountCreationDto accountCreationDto = new AccountCreationDto();
        accountCreationDto.setDocumentNumber(""); // fails validation
        ResponseEntity<AccountResponseDto> response = testRestTemplate
                .postForEntity("/accounts", accountCreationDto, AccountResponseDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldFindValidAccount(){
        ResponseEntity<AccountResponseDto> response = testRestTemplate
                .getForEntity("/accounts/" + savedAccountId, AccountResponseDto.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1122334455", response.getBody().getDocumentNumber());
    }

    @Test
    void shouldNotFindInvalidAccount(){
        ResponseEntity<AccountResponseDto> response = testRestTemplate
                .getForEntity("/accounts/1234", AccountResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnServerError_InvalidAccount(){
        ResponseEntity<AccountResponseDto> response = testRestTemplate
                .getForEntity("/accounts/incorrect_data_type", AccountResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


}
