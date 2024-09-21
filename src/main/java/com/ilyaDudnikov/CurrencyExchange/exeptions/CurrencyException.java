package com.ilyaDudnikov.CurrencyExchange.exeptions;

public class CurrencyException extends RuntimeException {
    public CurrencyException(String message) {
        super(message);
    }

    public CurrencyException(Throwable e) {
        super(e);
    }
}
