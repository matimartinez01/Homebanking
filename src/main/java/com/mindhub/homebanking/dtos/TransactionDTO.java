package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;

import java.time.LocalDate;

public class TransactionDTO {
    private Long id;
    private Double amount;
    private String description;
    private LocalDate date;
    private TransactionType type;


    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }
}


