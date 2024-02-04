package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepositories;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.HashSet;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initdata(ClientRepository clientRepository, AccountRepositories accountRepositories){
		return args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", new HashSet<>());
			Client client2 = new Client("Matias", "Martinez", "matimartinez@mindhub.com", new HashSet<>());
			Client client3 = new Client("Paula", "Ferrero", "paulaferrero@mindhub.com", new HashSet<>());
			Account account1 = new Account("2050", 15.0, LocalDate.now());
			Account account2 = new Account("5050", 20.0, LocalDate.now());
			Account account3 = new Account("1905", 50.0, LocalDate.now());
			Account account4 = new Account("305", 32.3, LocalDate.now());
			Account account5 = new Account("6905", 20.0, LocalDate.now());
			Account account6 = new Account("19005", 17.9, LocalDate.now());
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			client3.addAccount(account5);
			client3.addAccount(account6);
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			accountRepositories.save(account1);
			accountRepositories.save(account2);
			accountRepositories.save(account3);
			accountRepositories.save(account4);
			accountRepositories.save(account5);
			accountRepositories.save(account6);
		};
	}

}
