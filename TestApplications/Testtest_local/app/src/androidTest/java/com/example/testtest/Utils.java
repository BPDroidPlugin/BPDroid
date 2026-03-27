package com.example.testtest;

import java.util.Random;

public class Utils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateRandomString(int length){

        StringBuilder generatedString = new StringBuilder(length);
        Random random = new Random();

        for(int i = 0; i < length; i++){
            generatedString.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return generatedString.toString();
    }
}

