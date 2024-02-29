package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.TransactionType;

import java.time.LocalDate;

public record TransactionRequestDTO(Double amount, String description, String sourceAccount, String destinationAccount) {
}
