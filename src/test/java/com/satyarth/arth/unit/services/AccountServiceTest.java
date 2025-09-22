package com.satyarth.arth.unit.services;

import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.repo.AccountRepo;
import com.satyarth.arth.services.AccountService;
import com.satyarth.arth.utils.AccountUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepo accountRepo;

    @InjectMocks
    private AccountService accountService;

    private AccountCreationDto accountCreationDto;
    private Account account;
    private AccountResponseDto accountResponseDto;

    @BeforeEach
    void setUp() {
        accountCreationDto = new AccountCreationDto();
        accountCreationDto.setDocumentNumber("0123456789");

        account = new Account();
        account.setId(1L);
        account.setDocumentNumber("0123456789");

        accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(1L);
        accountResponseDto.setDocumentNumber("0123456789");
    }

    @Test
    void createAccount_ValidInput_ReturnsAccountResponseDto() {

        when(accountRepo.save(any())).thenReturn(account);

        try (MockedStatic<AccountUtils> accountUtilsMock = mockStatic(AccountUtils.class)) {
            accountUtilsMock.when(() -> AccountUtils.toDto(any(Account.class)))
                    .thenReturn(accountResponseDto);

            AccountResponseDto result = accountService.create(accountCreationDto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("0123456789", result.getDocumentNumber());
        }
    }

    @Test
    void createAccount_RepositoryThrowsException_PropagatesException() {

        when(accountRepo.save(any(Account.class))).thenThrow(new RuntimeException("DATABASE ERROR"));

        assertThrows(RuntimeException.class, () -> accountService.create(accountCreationDto));
        verify(accountRepo).save(any(Account.class));
    }

    @Test
    void getAccount_ValidId_ReturnsAccountResponseDto() {

        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));

        try (MockedStatic<AccountUtils> accountUtilsMock = mockStatic(AccountUtils.class)) {
            accountUtilsMock.when(() -> AccountUtils.toDto(any(Account.class)))
                    .thenReturn(accountResponseDto);

            AccountResponseDto result = accountService.getAccount(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("0123456789", result.getDocumentNumber());
            verify(accountRepo).findById(1L);
            accountUtilsMock.verify(() -> AccountUtils.toDto(any(Account.class)));
        }
    }


    @Test
    void getAccount_NonExistentId_ThrowsAccountNotFoundException() {

        when(accountRepo.findById(1L)).thenReturn(Optional.empty());

       assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getAccount(1L)
        );
        verify(accountRepo).findById(1L);
    }

    @Test
    void getAccount_NullId_ThrowsIllegalArgumentException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.getAccount(null)
        );
        verify(accountRepo, never()).findById(anyLong());
    }

    @Test
    void getAccount_ZeroId_ThrowsIllegalArgumentException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> accountService.getAccount(0L)
        );
        verify(accountRepo, never()).findById(anyLong());
    }



}
