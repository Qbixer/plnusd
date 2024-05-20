package com.example.plnusd.exception;

public class SameCurrencyException extends RuntimeException {

    public SameCurrencyException(String message) {
        super(message);
    }
}
