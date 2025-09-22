package com.satyarth.arth.services;

import com.satyarth.arth.constants.ServiceConstants;
import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.exceptions.AccountNotFoundException;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.repo.AccountRepo;
import com.satyarth.arth.utils.AccountUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public AccountResponseDto create(AccountCreationDto accountCreationDto){
        Account account = accountRepo.save(AccountUtils.toAccount(accountCreationDto));
        return AccountUtils.toDto(account);
    }

    public AccountResponseDto getAccount(Long id){
        if (id == null || id <= 0){
            throw new IllegalArgumentException(ServiceConstants.AccountService.INVALID_ID);
        }
        return accountRepo.findById(id)
                .map(AccountUtils::toDto)
                .orElseThrow(() -> new AccountNotFoundException(ServiceConstants.AccountService.NOT_FOUND));
    }


}
