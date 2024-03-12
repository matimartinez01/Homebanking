package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

	/*@Autowired
	private PasswordEncoder passwordEncoder;*/

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initdata(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {
			/*
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("Melba123"));
			Client client2 = new Client("Matias", "Martinez", "matimartinez@mindhub.com", passwordEncoder.encode("Mati123"));
			Client client3 = new Client("Juan", "Perez", "juanperez@mindhub.com", passwordEncoder.encode("Juan123"));

			Account account1 = new Account("VIN00000001", 250000.0, LocalDate.now());
			Account account2 = new Account("VIN00000002", 200000.0, LocalDate.now());

			Account account3 = new Account("VIN00000003", 200000.0, LocalDate.now());
			Account account4 = new Account("VIN00000004", 10000.0, LocalDate.now());

			Account account5 = new Account("VIN00000005", 155000.0, LocalDate.now());

			Loan mortgage = new Loan("Mortgage", 500000.0, Set.of(12, 24, 36, 48, 60));
			Loan personal = new Loan("Personal", 100000.0, Set.of(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000.0, Set.of(6, 12, 24, 36));




			Card card1 = new Card("2040-2011-8888-1010", "123", LocalDate.now(), LocalDate.now().plusYears(5), CardColor.GOLD, CardType.DEBIT);
			Card card2 = new Card("2209-2022-2606-2011", "545", LocalDate.now(), LocalDate.now().plusYears(5), CardColor.TITANIUM, CardType.CREDIT);
			Card card3 = new Card("2209-2022-2606-2011", "321", LocalDate.now(), LocalDate.now().plusYears(5), CardColor.SILVER, CardType.CREDIT);
			Card card4 = new Card("2209-2022-2606-2011", "349", LocalDate.now(), LocalDate.now().plusYears(5), CardColor.SILVER, CardType.DEBIT);


			client1.addCards(card1);
			client1.addCards(card2);
			client1.addCards(card3);
			client1.addCards(card4);


			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			client2.addAccount(account4);
			client3.addAccount(account5);


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);



			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);*/







		};
	}

}
