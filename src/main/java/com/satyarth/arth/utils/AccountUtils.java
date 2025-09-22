package com.satyarth.arth.utils;

import com.satyarth.arth.dto.AccountCreationDto;
import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.models.Account;

public class AccountUtils {

    public static AccountResponseDto toDto(Account account){
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(account.getId());
        dto.setDocumentNumber(account.getDocumentNumber());
        return dto;
    }

    public static Account toAccount(AccountCreationDto dto){
        Account account = new Account();
        account.setDocumentNumber(dto.getDocumentNumber());
        account.setCreationTime(System.currentTimeMillis());
        return account;
    }
}
