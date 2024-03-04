package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;
import com.mindhub.homebanking.Models.Client;
import org.springframework.context.annotation.Bean;

import java.util.List;


public interface CardService {

    List<Card> getClientCards(Client client);

    CardColor getCardColor(String cardColor);

    CardType getCardType(String cardType);

    List<Boolean> cardColorAndCardType(List<Card> cards, CardColor cardColor, CardType cardType);

    void cardSave(Card card);

}
