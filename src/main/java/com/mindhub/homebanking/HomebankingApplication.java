package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initdata(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Matias", "Martinez", "matimartinez@mindhub.com");

			Account account1 = new Account("2050", 15.0, LocalDate.now());
			Account account2 = new Account("5050", 20.0, LocalDate.now());

			Transaction transaction1 = new Transaction(10.5, "Hola", LocalDate.now(), TransactionType.Debit);
			Transaction transaction2 = new Transaction(20.5, "Chau", LocalDate.now(), TransactionType.Credit);

			Loan mortgage = new Loan("Mortgage", 500000.0, Set.of(12, 24, 36, 48, 60));
			Loan personal = new Loan("Personal", 100000.0, Set.of(6, 12, 24));
			Loan automotive = new Loan("Automotive", 300000.0, Set.of(6, 12, 24, 36));

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client1, mortgage);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client1, personal);


			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, client2, personal);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, client2, automotive);

			Card card1 = new Card("2040-2011-8888-1010", "123", LocalDate.now(), LocalDate.now().plusYears(5), CardColor.GOLD, CardType.DEBIT);
			Card card2 = new Card("2209-2022-2606-2011", "345", LocalDate.now(), LocalDate.now().plusYears(5), CardColor.TITANIUM, CardType.CREDIT);

			client1.addCards(card1);
			client1.addCards(card2);


			mortgage.addClientLoans(clientLoan1);
			personal.addClientLoans(clientLoan2);
			personal.addClientLoans(clientLoan3);
			automotive.addClientLoans(clientLoan4);

			client1.addClientLoans(clientLoan1);
			client1.addClientLoans(clientLoan2);

			client2.addClientLoans(clientLoan3);
			client2.addClientLoans(clientLoan4);


			account1.addTransactions(transaction1);
			account1.addTransactions(transaction2);

			client1.addAccount(account1);
			client1.addAccount(account2);

			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);

			loanRepository.save(mortgage);
			loanRepository.save(personal);
			loanRepository.save(automotive);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);




		};
	}

}
