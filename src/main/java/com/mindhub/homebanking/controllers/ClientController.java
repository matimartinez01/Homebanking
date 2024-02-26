package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;


    @GetMapping("/")
    public ResponseEntity<?> getAllClients(){
        List<Client> client = clientRepository.findAll();
        return new ResponseEntity<>(client.stream().map(ClientDTO::new).toList(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getOneClientById(@PathVariable Long id){
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ClientDTO clientDTO = new ClientDTO(client);
        return new ResponseEntity<>(clientDTO, HttpStatus.OK);
    }

}
