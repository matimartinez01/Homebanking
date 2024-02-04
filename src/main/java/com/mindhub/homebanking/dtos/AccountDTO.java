package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Account;

import java.time.LocalDate;

public class AccountDTO {

    private Long id;
    private String number;
    private Double balance;
    private LocalDate creationDate;

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Double getBalance() {
        return balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}
