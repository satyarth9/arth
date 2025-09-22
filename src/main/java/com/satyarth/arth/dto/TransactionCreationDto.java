package com.satyarth.arth.dto;

import com.satyarth.arth.constants.ValidationConstants;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class TransactionCreationDto {

    @NotNull
    private Long accountId;

    @Min(value = ValidationConstants.OperationType.MIN_OP_TYPE_ID, message = ValidationConstants.OperationType.INVALID_OP_TYPE)
    @Max(value = ValidationConstants.OperationType.MAX_OP_TYPE_ID, message = ValidationConstants.OperationType.INVALID_OP_TYPE)
    private Long operationTypeId;

    @Positive
    private BigDecimal amount;

    public TransactionCreationDto() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public Long getOperationTypeId() {
        return operationTypeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
