package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepositories;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @Transactional
    @PostMapping("/clients/current/loan")
    public ResponseEntity<?> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO){

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        List<String> clientAccounts = client.getAccounts().stream().map(account -> account.getNumber()).toList();
        List<LoanDTO> loans = loanRepositories.findAll().stream().map(LoanDTO::new).toList();
        Loan loan = loanRepositories.findById(loanApplicationDTO.loanID()).orElse(null);


        //REVISAR
        if(loanApplicationDTO.loanID().describeConstable().isEmpty() || loanApplicationDTO.loanID() == null){
            return new ResponseEntity<>("You have to complete the field 'loanID'", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.amount() == null || loanApplicationDTO.amount() <= 0){
            return new ResponseEntity<>("You have to complete the field 'amount' with a number larger than 0", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.payments() == null || loanApplicationDTO.payments() <= 0){
            return new ResponseEntity<>("You have to complete the field 'payments' and larger than 0", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.accountDestination().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'accountDestination'", HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("There isn't any loan with the id " + loanApplicationDTO.loanID(), HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.amount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The max amount you can ask in the loan " + loan.getName() + " is" + loan.getMaxAmount(), HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.payments())){
            return new ResponseEntity<>("The payments available in the loan " + loan.getName() + " are" + loan.getPayments(), HttpStatus.FORBIDDEN);
        }

        //Está bien o debería decir que no se puede a esa cuenta directamente
        if(accountRepository.findByNumber(loanApplicationDTO.accountDestination()) == null){
            return new ResponseEntity<>("The account " + loanApplicationDTO.accountDestination() + " doesn't exist", HttpStatus.FORBIDDEN);

        }

        if(!clientAccounts.contains(loanApplicationDTO.accountDestination())){
            return new ResponseEntity<>("You can't ask a loan for the account " + loanApplicationDTO.accountDestination(), HttpStatus.FORBIDDEN);
        }

        Account account = accountRepository.findByNumber(loanApplicationDTO.accountDestination());

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.amount() * 1.2, loanApplicationDTO.payments());
        Transaction transaction = new Transaction(loanApplicationDTO.amount(), loan.getName() + " loan approved", LocalDate.now(), TransactionType.Credit);

        client.addClientLoans(clientLoan);
        loan.addClientLoans(clientLoan);
        account.addTransactions(transaction);

        account.setBalance(account.getBalance() + loanApplicationDTO.amount());

        transactionRepository.save(transaction);
        accountRepository.save(account);
        clientLoanRepository.save(clientLoan);
        clientRepository.save(client);
        loanRepositories.save(loan);

        return new ResponseEntity<>("CREATED", HttpStatus.CREATED);

    }

    @GetMapping("/clients/current/loan")
    public ResponseEntity<?> getLoans() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);


        return new ResponseEntity<>(client.getLoans(), HttpStatus.OK);
        }


    }




