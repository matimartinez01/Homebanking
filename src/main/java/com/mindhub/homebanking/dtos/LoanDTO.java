package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.ClientLoan;
import com.mindhub.homebanking.Models.Loan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoanDTO {

    private Long id;
    private String name;
    private Double maxAmount;
    private Set<Integer> payments;
    private List<ClientLoanDTO> clientLoans;
    private Long client;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.clientLoans = loan.getClientLoans().stream().map(ClientLoanDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public Set<Integer> getPayments() {
        return payments;
    }

    public List<ClientLoanDTO> getClientLoans() {
        return clientLoans;
    }
}
