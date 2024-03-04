package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.services.implementsService.CardServiceImplement;
import com.mindhub.homebanking.services.implementsService.ClientServiceImplement;
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

    //@Autowired
    //ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;



    @Autowired
    ClientServiceImplement clientServiceImplement;

    @Autowired
    CardServiceImplement cardServiceImplement;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<?> addCard(@RequestBody CardRequestDTO cardRequestDTO){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientServiceImplement.getClientByEmail(userEmail);
        List<Card> cards = cardServiceImplement.getClientCards(client);


        if (cardRequestDTO.cardColor().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'cardColor'", HttpStatus.FORBIDDEN);
        }

        if(!cardRequestDTO.cardColor().equals("TITANIUM") && !cardRequestDTO.cardColor().equals("GOLD") && !cardRequestDTO.cardColor().equals("SILVER")){
            return new ResponseEntity<>("The colors of the card you can choose are: TITANIUM, SILVER or GOLD", HttpStatus.FORBIDDEN);
        }

        if (cardRequestDTO.cardType().isBlank()){
            return new ResponseEntity<>("You have to complete the field 'cardType'", HttpStatus.FORBIDDEN);
        }

        if(!cardRequestDTO.cardType().equals("DEBIT") && !cardRequestDTO.cardType().equals("CREDIT")){
            return new ResponseEntity<>("The type of the card you can choose are: CREDIT or DEBIT", HttpStatus.FORBIDDEN);
        }


        CardColor cardColor = cardServiceImplement.getCardColor(cardRequestDTO.cardColor());
        CardType cardType = cardServiceImplement.getCardType(cardRequestDTO.cardType());
        List <Boolean> cardsTypeColor = cardServiceImplement.cardColorAndCardType(cards, cardColor, cardType);

        if(cardsTypeColor.contains(true)){
            return new ResponseEntity<>("You already have one card with type " + cardRequestDTO.cardType() + " and color " + cardRequestDTO.cardColor(), HttpStatus.FORBIDDEN);
        }

        String number = MathRandom.getNumberCard();

        while (cardRepository.findByNumber(number) != null){
            number = MathRandom.getNumberCard();
        }

        Card card = new Card(number, MathRandom.getCvv(), LocalDate.now(), LocalDate.now().plusYears(5), cardColor, cardType);

        cardServiceImplement.cardSave(card);
        client.addCards(card);
        clientServiceImplement.saveClient(client);


        return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
    }



    @GetMapping("/clients/current/cards")
    public ResponseEntity<?> getCards(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientServiceImplement.getClientByEmail(userEmail);
        Set<Card> cards = new HashSet<>();
        cards = client.getCards();

        return ResponseEntity.ok(cards.stream().map(CardDTO::new).toList());
    }


}
