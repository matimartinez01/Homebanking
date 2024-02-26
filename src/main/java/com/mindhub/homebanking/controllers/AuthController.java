package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoginDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.services.JwtUtilService;
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
        clientRepository.save(newClient);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);

    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        var mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok("Hello " + mail);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);

        return ResponseEntity.ok(new ClientDTO(client));
    }

}
