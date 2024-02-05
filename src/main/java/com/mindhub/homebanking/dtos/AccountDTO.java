package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class AccountDTO {

    private Long id;
    private String number;
    private Double balance;
    private LocalDate creationDate;
    private List<TransactionDTO> transactions;

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).toList();
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

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }
}
