package com.satyarth.arth.dto;

import java.math.BigDecimal;

public class TransactionResponseDto {

    private Long id;
    private Long accountId;
    private Long operationTypeId;
    private BigDecimal amount;
    private Long eventDateTime;

    public TransactionResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Long eventDateTime) {
        this.eventDateTime = eventDateTime;
    }
}
