package com.valpuestajorge.conecta4.security.service;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class PasswordGenerator {

    private PasswordGenerator(){}
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "#$%&*/+-=";
    private static final SecureRandom RANDOM = new SecureRandom();



    public static String generateRandomPassword(int length) {
        if (length < 4) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length-4);
        int counter;
        for (int i = 0; i < length-4; i++) {
            counter = RANDOM.nextInt(4);
            char nextChar = getRandomChar(counter);
            sb.append(nextChar);
        }
        return checkDuplicatedValues(complementPassword(sb));
    }

    private static String checkDuplicatedValues(String password) {
        String result = password;
        for(int pos = 0; pos< password.length(); pos++){
            char currentChar = result.charAt(pos);
            if(result.indexOf(currentChar,pos+1) - pos > -1){
                char aNewChar = getNewChar(currentChar);
                result = result.substring(0,pos) + aNewChar + result.substring(pos+1);
            }
        }
        if(hasDuplicatedValues(result)){
            result = checkDuplicatedValues(result);
        }
        return result;
    }

    private static boolean hasDuplicatedValues(String result) {
        for(int pos = 0; pos< result.length(); pos++){
            char currentChar = result.charAt(pos);
            if(result.indexOf(currentChar,pos+1) - pos > -1){
                return true;
            }
        }
        return false;
    }

    private static char getNewChar(char currentChar) {
        char newChar = currentChar;
        String group = "";
        if(CHAR_LOWER.indexOf(currentChar)>-1){
            group = CHAR_LOWER;
        } else if (CHAR_UPPER.indexOf(currentChar)>-1) {
            group = CHAR_UPPER;
        } else if (NUMBERS.indexOf(currentChar)>-1) {
            group = NUMBERS;
        } else if (SPECIAL_CHARACTERS.indexOf(currentChar)>-1) {
            group = SPECIAL_CHARACTERS;
        }
        while (newChar == currentChar){
            newChar = getRandomCharacter(group);
        }
        return newChar;
    }

    private static String complementPassword(StringBuilder sb) {
        String randomPassword = sb.toString();
        char newChar = getRandomChar(0);
        randomPassword += newChar;
        newChar = getRandomChar(1);
        randomPassword += newChar;
        newChar = getRandomChar(2);
        randomPassword += newChar;
        newChar = getRandomChar(3);
        randomPassword += newChar;
        return randomPassword;
    }

    private static char getRandomChar(int type){
        return switch(type) {
            case 0 -> getRandomCharacter(CHAR_UPPER);
            case 1 -> getRandomCharacter(CHAR_LOWER);
            case 2 -> getRandomCharacter(NUMBERS);
            case 3 -> getRandomCharacter(SPECIAL_CHARACTERS);
            default -> ' ';
        };
    }

    private static char getRandomCharacter(String group){
        int rndCharAt = RANDOM.nextInt(group.length());
        return group.charAt(rndCharAt);
    }

}