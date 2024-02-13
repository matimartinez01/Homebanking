package com.mindhub.homebanking.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number, cvv;
    private LocalDate fromDate, trhuDate;
    private CardColor cardColor;
    private CardType cardType;

    private String cardHolder;
    @ManyToOne
    private Client client;

    public Card() {}

    public Card(String number, String cvv, LocalDate fromDate, LocalDate trhuDate, CardColor cardColor, CardType cardType) {
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.trhuDate = trhuDate;
        this.cardColor = cardColor;
        this.cardType = cardType;
    }

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public LocalDate getTrhuDate() {
        return trhuDate;
    }

    public void setTrhuDate(LocalDate trhuDate) {
        this.trhuDate = trhuDate;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
