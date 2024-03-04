package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.context.annotation.Bean;

import java.util.List;


public interface ClientService {
    List<Client> getAllClients();

    List<ClientDTO> getAllClientsDTO();

    Client getClientById(Long id);

    Client getClientByEmail(String email);

    void saveClient(Client client);

}
