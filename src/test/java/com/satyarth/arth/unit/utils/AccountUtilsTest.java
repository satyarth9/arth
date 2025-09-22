package com.satyarth.arth.unit.utils;

import com.satyarth.arth.dto.AccountResponseDto;
import com.satyarth.arth.models.Account;
import com.satyarth.arth.utils.AccountUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountUtilsTest {

    @Test
    void toDto_ValidAccount_ReturnsCorrectDto() {
        Account account = new Account();
        account.setId(1L);
        account.setDocumentNumber("0123456789");
        account.setCreationTime(System.currentTimeMillis());

        AccountResponseDto result = AccountUtils.toDto(account);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("0123456789", result.getDocumentNumber());
    }

}
