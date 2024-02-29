package com.mindhub.homebanking.dtos;

    public record LoanApplicationDTO(Long loanID, Double amount, Integer payments, String accountDestination) {
}
