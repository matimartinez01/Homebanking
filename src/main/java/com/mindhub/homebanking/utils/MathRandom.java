package com.mindhub.homebanking.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MathRandom {
    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getNumberCard(){
        String number = getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999) + "-" + getRandomNumber(1000, 9999);
        return number;
    }

    public static String getCvv(){
        String number = String.format("%03d", getRandomNumber(0, 999));
        return number;
    }

    public static String getAccountNumber(){
        String number = "VIN" + String.format("%08d",getRandomNumber(1, 99999999));
        return number;
    }
}
