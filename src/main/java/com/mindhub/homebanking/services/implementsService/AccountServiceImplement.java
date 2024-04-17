package com.mindhub.homebanking.services.implementsService;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.utils.MathRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> getAccounts(Client client) {
        return client.getAccounts().stream().toList();
    }

    @Override
    public String generateAccountNumber() {
        String number = MathRandom.getAccountNumber();
        while (accountRepository.findByNumber(number) != null){
            number = MathRandom.getAccountNumber();
        };
        return number;
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<String> getAccountsNumber(Client client) {
        return client.getAccounts().stream().map(account -> account.getNumber()).toList();
    }

}
