package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.implementsService.ClientServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    ClientServiceImplement clientServiceImplement;


    @GetMapping("/")
    public ResponseEntity<?> getAllClients(){
        return new ResponseEntity<>(clientServiceImplement.getAllClientsDTO(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getOneClientById(@PathVariable Long id){
        Client client = clientServiceImplement.getClientById(id);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<?> getClient() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientServiceImplement.getClientByEmail(userEmail);
        return ResponseEntity.ok(new ClientDTO(client));
    }

}
