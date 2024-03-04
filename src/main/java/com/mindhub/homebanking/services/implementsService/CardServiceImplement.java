package com.mindhub.homebanking.services.implementsService;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImplement implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Override
    public List<Card> getClientCards(Client client) {
        return client.getCards().stream().toList();
    }

    @Override
    public CardColor getCardColor(String cardColor) {
        return CardColor.valueOf(cardColor);
    }

    @Override
    public CardType getCardType(String cardType) {
        return CardType.valueOf(cardType);
    }

    @Override
    public List<Boolean> cardColorAndCardType(List<Card> cards, CardColor cardColor, CardType cardType) {
        return cards.stream().map(card -> card.getCardType() == cardType && card.getCardColor() == cardColor).toList();
    }

    @Override
    public void cardSave(Card card) {
        cardRepository.save(card);
    }
}
