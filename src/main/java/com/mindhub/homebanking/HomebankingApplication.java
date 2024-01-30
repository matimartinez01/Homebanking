package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initdata(ClientRepository clientRepository){
		return args -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Matias", "Martinez", "matimartinez@mindhub.com");
			Client client3 = new Client("Paula", "Ferrero", "paulaferrero@mindhub.com");
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
		};
	}

}
