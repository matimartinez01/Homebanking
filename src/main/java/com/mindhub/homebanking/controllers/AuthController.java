package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.SecurityServices.JwtUtilService;
import com.mindhub.homebanking.utils.MathRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MathRandom mathRandom;

    @Autowired
    private AccountRepository accountRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){

        Client client = clientRepository.findByEmail(loginDTO.email());

        if(client == null){
            return ResponseEntity.status(400).body("Email or password incorrect");
        }

        if(!passwordEncoder.matches(loginDTO.password(), client.getPassword())){
            return ResponseEntity.status(400).body("Email or password incorrect");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.email());
        final String jwt = jwtUtilService.generateToken(userDetails);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO){

        if(registerDTO.firstName().isBlank()){
            return ResponseEntity.status(400).body("The name can't be empty");
        }

        if(registerDTO.lastName().isBlank()){
            return ResponseEntity.status(400).body("The last name can't be empty");
        }

        if(registerDTO.email().isBlank()){
            return ResponseEntity.status(400).body("The email can't be empty");
        }

        if(!registerDTO.email().contains("@")){
            return ResponseEntity.status(400).body("You have to enter an email");

        }


        if(registerDTO.password().length() < 6){
            return ResponseEntity.status(400).body("The password needs at least 6 characters");
        }

        if(clientRepository.findByEmail(registerDTO.email()) != null){
            return ResponseEntity.status(400).body("There is an account with that email");
        }


        Client newClient = new Client(registerDTO.firstName(), registerDTO.lastName(), registerDTO.email(), passwordEncoder.encode(registerDTO.password()));

        String number = mathRandom.getAccountNumber();

        while (accountRepository.findByNumber(number) != null){
            number = mathRandom.getAccountNumber();
        }


        Account account = new Account(number, 0.0, LocalDate.now());
        newClient.addAccount(account);
        clientRepository.save(newClient);
        accountRepository.save(account);

        return new ResponseEntity<>("Created", HttpStatus.CREATED);

    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        var mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("Hello " + mail);
    }

    @GetMapping("/test2")
    public ResponseEntity<?> test2(){
        Account account = accountRepository.findByNumber("VIN-00001");
        AccountDTO client2 = new AccountDTO(account);
        return ResponseEntity.ok(client2);
    }




}
