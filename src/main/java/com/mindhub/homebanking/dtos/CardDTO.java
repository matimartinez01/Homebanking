package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private String cvv;
    private LocalDate fromDate, trhuDate;

    private CardColor cardColor;
    private CardType cardType;
    private String nameClient, number;

    public CardDTO(Card card){
        this.id = card.getId();
        this.cvv = card.getCvv();
        this.number = card.getNumber();
        this.fromDate = card.getFromDate();
        this.trhuDate = card.getTrhuDate();
        this.cardColor = card.getCardColor();
        this.cardType = card.getCardType();
        this.nameClient = card.getClient().getFirstName() + " " + card.getClient().getLastName();
    }

    public Long getId() {
        return id;
    }

    public String getCvv() {
        return cvv;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getTrhuDate() {
        return trhuDate;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getNameClient() {
        return nameClient;
    }
}
