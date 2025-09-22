package com.satyarth.arth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "operation_types")
public class OperationType {

    @Id
    private Long id;

    private String description;

    private Boolean credit;

    public OperationType() {
    }

    public OperationType(Long id, String description, Boolean credit) {
        this.id = id;
        this.description = description;
        this.credit = credit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCredit() {
        return credit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }
}
