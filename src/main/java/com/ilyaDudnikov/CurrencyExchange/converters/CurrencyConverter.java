package com.ilyaDudnikov.CurrencyExchange.converters;

import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.models.Currency;

public class CurrencyConverter {
    public static Currency convertToEntity(CurrencyDto currencyDto) {
        return Currency.builder()
                .id(currencyDto.getId())
                .code(currencyDto.getCode())
                .fullName(currencyDto.getFullName())
                .sign(currencyDto.getSign())
                .build();
    }

    public static CurrencyDto convertToDto(Currency currency) {
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .fullName(currency.getFullName())
                .sign(currency.getSign())
                .build();
    }
}
