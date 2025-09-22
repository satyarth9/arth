package com.satyarth.arth.dto;

import com.satyarth.arth.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AccountCreationDto {

    @NotBlank(message = ValidationConstants.DocumentNumber.BLANK_MESSAGE)
    @Size(min=ValidationConstants.DocumentNumber.MIN_LENGTH, message = ValidationConstants.DocumentNumber.SIZE_MESSAGE)
    private String documentNumber;

    public AccountCreationDto() {
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
