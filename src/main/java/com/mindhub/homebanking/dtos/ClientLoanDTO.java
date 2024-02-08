package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.ClientLoan;
import com.mindhub.homebanking.Models.Loan;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class ClientLoanDTO {

    private Long id;
    private Double amount;
    private Integer payments;
    private String name;
    private Long loanId;


    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.name = clientLoan.getLoan().getName();
        this.loanId = clientLoan.getLoan().getId();
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getName() {
        return name;
    }

    public Long getLoanId() {
        return loanId;
    }


    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", payments=" + payments +
                ", name='" + name + '\'' +
                ", loanId=" + loanId +
                '}';
    }
}
