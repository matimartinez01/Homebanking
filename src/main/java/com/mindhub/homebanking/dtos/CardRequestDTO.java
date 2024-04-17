package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;

public record CardRequestDTO(String cardColor, String cardType) {
}
