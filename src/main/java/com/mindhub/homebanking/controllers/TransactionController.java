package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.dtos.TransactionRequestDTO;
import com.mindhub.homebanking.services.implementsService.AccountServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {


    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountServiceImplement accountServiceImplement;


    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<?> addTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO){

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        List<String> accountsClient = accountServiceImplement.getAccountsNumber(client);


        if(transactionRequestDTO.destinationAccount().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'destinationAccount'", HttpStatus.FORBIDDEN);
        }

        if(transactionRequestDTO.sourceAccount().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'sourceAccount'", HttpStatus.FORBIDDEN);
        }

        if(transactionRequestDTO.description().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'description'", HttpStatus.FORBIDDEN);
        }


        if(transactionRequestDTO.amount() == null){
            return new ResponseEntity<>("You have to complete the field 'amount' with a number", HttpStatus.FORBIDDEN);
        }

        if(transactionRequestDTO.amount() <= 0){
            return new ResponseEntity<>("The amount of the transaction has to be more than 0", HttpStatus.FORBIDDEN);
        }

        if (transactionRequestDTO.sourceAccount().equals(transactionRequestDTO.destinationAccount())){
            return new ResponseEntity<>("The source account needs to be different with the destination account", HttpStatus.FORBIDDEN);
        }

        if (!accountsClient.contains(transactionRequestDTO.sourceAccount())){
            return new ResponseEntity<>("You don't have account with the number: " + transactionRequestDTO.sourceAccount(), HttpStatus.FORBIDDEN);
        }

        Account sourceAccount = accountRepository.findByNumber(transactionRequestDTO.sourceAccount());

        if (accountRepository.findByNumber(transactionRequestDTO.destinationAccount()) == null){
            return new ResponseEntity<>("There isn't any account with the number: " + transactionRequestDTO.destinationAccount(), HttpStatus.FORBIDDEN);
        }

        Account destinationAccount = accountRepository.findByNumber(transactionRequestDTO.destinationAccount());

        if (transactionRequestDTO.amount() > sourceAccount.getBalance()){
            return new ResponseEntity<>("The account " + sourceAccount.getNumber() + " doesn't have sufficient amounts to make the transaction", HttpStatus.FORBIDDEN);
        }

        Transaction transactionSourceAccount = new Transaction(-(transactionRequestDTO.amount()), transactionRequestDTO.description(), LocalDate.now(), TransactionType.Debit);
        Transaction transactionDestinationAccount = new Transaction(transactionRequestDTO.amount(), transactionRequestDTO.description(), LocalDate.now(), TransactionType.Credit);

        sourceAccount.addTransactions(transactionSourceAccount);
        destinationAccount.addTransactions(transactionDestinationAccount);
        sourceAccount.setBalance(sourceAccount.getBalance() - transactionRequestDTO.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionRequestDTO.amount());

        transactionRepository.save(transactionSourceAccount);
        transactionRepository.save(transactionDestinationAccount);
        accountServiceImplement.saveAccount(sourceAccount);
        accountServiceImplement.saveAccount(destinationAccount);



        return new ResponseEntity<>("Transaction successful", HttpStatus.OK);
    };

    @GetMapping("/clients/current/transactions")
    public ResponseEntity<?> getTransactions(){

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        Set<Account> accounts = new HashSet<>();
        accounts = client.getAccounts();

        return ResponseEntity.ok(accounts.stream().map(AccountDTO::new).toList());

    }


}
