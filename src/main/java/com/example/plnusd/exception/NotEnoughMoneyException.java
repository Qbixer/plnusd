package com.example.plnusd.exception;

public class NotEnoughMoneyException extends RuntimeException{

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}

