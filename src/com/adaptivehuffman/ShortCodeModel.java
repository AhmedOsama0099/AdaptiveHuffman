package com.adaptivehuffman;

public class ShortCodeModel {
   private char symbol;
    private String shortCode;

    public ShortCodeModel(char symbol, String shortCode) {
        this.symbol = symbol;
        this.shortCode = shortCode;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getShortCode() {
        return shortCode;
    }

}

