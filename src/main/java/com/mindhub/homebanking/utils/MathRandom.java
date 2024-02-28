package com.mindhub.homebanking.utils;

import org.springframework.stereotype.Component;

@Component
public class MathRandom {
    public int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getNumberCard(){
        String number = getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999);
        return number;
    }

    public String getCvv(){
        String number = String.format("%03d", getRandomNumber(0, 999));
        return number;
    }

    public String getAccountNumber(){
        String number = "VIN" + String.format("%08d",getRandomNumber(1, 99999999));
        return number;
    }
}
