package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.utils.MathRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/")
public class CardController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    MathRandom mathRandom;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> addCard(@RequestBody CardRequestDTO cardRequestDTO){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        Set<Card> cards = new HashSet<>();
        cards = client.getCards();

        List <Boolean> cardsTypeColor = cards.stream().map(card -> card.getCardType() == cardRequestDTO.cardType() && card.getCardColor() == cardRequestDTO.cardColor()).toList();

        if(cardsTypeColor.contains(true)){
            return new ResponseEntity<>("You already have one card with type " + cardRequestDTO.cardType() + " and color " + cardRequestDTO.cardColor(), HttpStatus.FORBIDDEN);
        }

        String number = mathRandom.getNumberCard();

        while (cardRepository.findByNumber(number) != null){
            number = mathRandom.getNumberCard();
        }

        Card card = new Card(number, mathRandom.getCvv(), LocalDate.now(), LocalDate.now().plusYears(5), cardRequestDTO.cardColor(), cardRequestDTO.cardType());

        cardRepository.save(card);
        client.addCards(card);
        clientRepository.save(client);


        return new ResponseEntity<>(cards.stream().map(CardDTO::new).toList(), HttpStatus.CREATED);
    }



    @GetMapping("/clients/current/cards")
    public ResponseEntity<?> getCards(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepository.findByEmail(userEmail);
        Set<Card> cards = new HashSet<>();
        cards = client.getCards();

        return ResponseEntity.ok(cards.stream().map(CardDTO::new).toList());
    }


}
