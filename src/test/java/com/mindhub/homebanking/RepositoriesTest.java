package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.services.implementsService.AccountServiceImplement;
import com.mindhub.homebanking.services.implementsService.CardServiceImplement;
import com.mindhub.homebanking.utils.MathRandom;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.util.Objects.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //Test unitarios

    @Test
    public void accountsNumber(){
        String number = MathRandom.getAccountNumber();
        assertThat(number.length(), equalTo(11));
        assertThat(number, startsWith("VIN"));
    }

    @Test
    public void cardsNumber(){
        String number = MathRandom.getNumberCard();
        assertThat(number.length(), equalTo(19));
    }

    @Test
    public void cardCVV(){
        String number = MathRandom.getCvv();
        assertThat(number.length(), equalTo(3));
    }


    //Test Integradores

    //AccountRepository

    @Test
    public void accountsNotNull(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts.size(), greaterThan(0));
    }

    @Test
    public void accountNumberUnique(){
        List<String> accounts = new java.util.ArrayList<>(accountRepository.findAll().stream().map(Account::getNumber).toList());
        //Para que falle
        //accounts.add("VIN00000001");
        Set<String> duplicates = new HashSet<>(accounts);
        assertThat(accounts.size(), equalTo(duplicates.size()));
    }


    //CardRepository
    @Test
    public void cardNumberUnique(){
        List<String> cardsNumber = new java.util.ArrayList<>(cardRepository.findAll().stream().map(Card::getNumber).toList());
        Set<String> duplicates = new HashSet<>(cardsNumber);
        assertThat(cardsNumber.size(), equalTo(duplicates.size()));
    }

    @Test
    public void cardExpiration() {
        List<Integer> cardDate = cardRepository.findAll().stream().map(a -> a.getTrhuDate().compareTo(a.getFromDate())).toList();
        assertThat(cardDate, everyItem(is(5)));
    }

    //ClientRepository

    @Test
    public void emailUnique(){
        List<String> emails = new java.util.ArrayList<>(clientRepository.findAll().stream().map(Client::getEmail).toList());
        //Prueba para verificar
        //emails.add("melba@mindhub.com");
        Set<String> duplicates = new HashSet<>(emails);
        assertThat(emails.size(), equalTo(duplicates.size()));
    }

    @Test
    public void firstNameAndLastNameAndEmail(){
        List<Integer> names = clientRepository.findAll().stream().map(a -> a.getFirstName().length()).toList();
        List<Integer> lastNames = clientRepository.findAll().stream().map(a -> a.getLastName().length()).toList();
        List<String> emails = new java.util.ArrayList<>(clientRepository.findAll().stream().map(Client::getEmail).toList());
        assertThat(names, everyItem(greaterThan(0)));
        assertThat(lastNames, everyItem(greaterThan(0)));
        assertThat(emails, everyItem(containsString("@")));
    }

    //LoanRepository

    @Test
    public void getLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void maxAmount(){
        List<Double> amounts = loanRepository.findAll().stream().map(Loan::getMaxAmount).toList();
        assertThat(amounts, everyItem(greaterThan(0.0)));
    }

    //ClientLoanRepository

    @Test
    public void getMortgageLoans(){
        Loan mortgage = loanRepository.findByName("Mortgage");
        List<ClientLoan> clientLoans = clientLoanRepository.findAll().stream().filter(a -> a.getLoan() == mortgage).toList();
        assertThat(clientLoans, is(not(empty())));
    }

    @Test
    public void maxAmountMortgageLoans(){
        Loan mortgage = loanRepository.findByName("Mortgage");
        Double mortgageMaxAmount = mortgage.getMaxAmount();
        List<Double> amountMortgageLoans = clientLoanRepository.findAll().stream().filter(a -> a.getLoan() == mortgage).map(ClientLoan::getAmount).toList();
        //Para que falle
        //amountMortgageLoans.add(501000.0);
        assertThat(amountMortgageLoans, everyItem(lessThan(mortgageMaxAmount)));
    }

    //TransactionRepository

    @Test
    public void debitTransaction(){
        List<Double> debitTransactionAmount = transactionRepository.findAll().stream().filter(a -> a.getType() == TransactionType.Debit).map(Transaction::getAmount).toList();
        assertThat(debitTransactionAmount, everyItem(lessThan(0.0)));
    }

    @Test
    public void creditTransactions(){
        List<Double> creditTransactions = transactionRepository.findAll().stream().filter(a -> a.getType() == TransactionType.Credit).map(Transaction::getAmount).toList();
        assertThat(creditTransactions, everyItem(greaterThan(0.0)));
    }


}
