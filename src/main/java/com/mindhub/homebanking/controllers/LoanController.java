package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.services.implementsService.AccountServiceImplement;
import com.mindhub.homebanking.services.implementsService.ClientServiceImplement;
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

    @Autowired
    AccountServiceImplement accountServiceImplement;

    @Autowired
    ClientServiceImplement clientServiceImplement;

    @Transactional
    @PostMapping("/clients/current/loan")
    public ResponseEntity<?> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO){

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        List<String> clientAccounts = accountServiceImplement.getAccountsNumber(client);
        Loan loan = loanRepositories.findById(loanApplicationDTO.loanID()).orElse(null);


        //REVISAR
        if(loanApplicationDTO.loanID().describeConstable().isEmpty() || loanApplicationDTO.loanID() == null){
            return new ResponseEntity<>("You have to complete the field 'loanID'", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.amount() == null || loanApplicationDTO.amount() <= 0){
            return new ResponseEntity<>("You have to complete the field 'amount' with a number larger than 0", HttpStatus.FORBIDDEN);
        }

        //Tiene sentido? Es un select en el front
        if(loanApplicationDTO.payments() <= 0){
            return new ResponseEntity<>("You have to complete the field 'payments' and larger than 0", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.accountDestination().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'accountDestination'", HttpStatus.FORBIDDEN);
        }

        //Tiene sentido? Es un select en el front
        if(loan == null){
            return new ResponseEntity<>("There isn't any loan with the id " + loanApplicationDTO.loanID(), HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.amount() > loan.getMaxAmount()){
            return new ResponseEntity<>("The max amount you can ask in the loan " + loan.getName() + " is" + loan.getMaxAmount(), HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.payments())){
            return new ResponseEntity<>("The payments available in the loan " + loan.getName() + " are" + loan.getPayments(), HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(loanApplicationDTO.accountDestination()) == null){
            return new ResponseEntity<>("The account is invalid", HttpStatus.FORBIDDEN);
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
        accountServiceImplement.saveAccount(account);
        clientLoanRepository.save(clientLoan);
        clientServiceImplement.saveClient(client);
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




