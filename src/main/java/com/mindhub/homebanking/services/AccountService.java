package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;

import java.awt.*;
import java.util.List;

public interface AccountService {

    List<Account> getAccounts(Client cLient);

    String generateAccountNumber();

    void saveAccount(Account account);

    List<String> getAccountsNumber(Client client);

}
