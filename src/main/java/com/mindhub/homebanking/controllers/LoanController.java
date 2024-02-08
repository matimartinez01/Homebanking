package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Repositories.LoanRepository;
import com.mindhub.homebanking.dtos.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
public class LoanController {
    @Autowired
    private LoanRepository loanRepositories;

    @GetMapping("/")
    public List<LoanDTO> getLoans(){
        return loanRepositories.findAll().stream().map(LoanDTO::new).toList();
    }
}
