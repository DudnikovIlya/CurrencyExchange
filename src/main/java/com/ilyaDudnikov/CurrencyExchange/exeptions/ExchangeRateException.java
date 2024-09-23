package com.ilyaDudnikov.CurrencyExchange.exeptions;

public class ExchangeRateException extends RuntimeException {
    public ExchangeRateException(String message) {
        super(message);
    }

    public ExchangeRateException(Throwable e) {
        super(e);
    }
}
