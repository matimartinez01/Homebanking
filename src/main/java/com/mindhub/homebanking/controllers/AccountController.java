package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.utils.MathRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    MathRandom mathRandom;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAccounts2(){
        List<AccountDTO> accountsDTO = accountRepository.findAll().stream().map(AccountDTO::new).toList();
        return new ResponseEntity<>(accountsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountByID(@PathVariable Long id){
        Account account = accountRepository.findById(id).orElse(null);
        if(account == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AccountDTO accountDTO = new AccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> addAccount() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        List<Account> accounts = new ArrayList<>();
        accounts = client.getAccounts().stream().toList();

        if (accounts.size() < 3){
            String number = mathRandom.getAccountNumber();
            while (accountRepository.findByNumber(number) != null){
                number = mathRandom.getAccountNumber();
            }
            Account account = new Account(number, 0.0, LocalDate.now());
            accountRepository.save(account);
            client.addAccount(account);
            clientRepository.save(client);
            return new ResponseEntity<>("Account created" ,HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("You have already 3 accounts" ,HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping("/clients/current/accounts")
    public ResponseEntity<?> getAccounts(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        Set<Account> accounts = new HashSet<>();
        accounts = client.getAccounts();

        return ResponseEntity.ok(accounts.stream().map(AccountDTO::new).toList());


    }


}
