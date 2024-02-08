package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountRepository accountRepositories;

    @GetMapping("/")
    public ResponseEntity<List<AccountDTO>> getAccounts(){
        List<AccountDTO> accountsDTO = accountRepositories.findAll().stream().map(AccountDTO::new).toList();
        return new ResponseEntity<>(accountsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountByID(@PathVariable Long id){
        Account account = accountRepositories.findById(id).orElse(null);
        if(account == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AccountDTO accountDTO = new AccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

}
