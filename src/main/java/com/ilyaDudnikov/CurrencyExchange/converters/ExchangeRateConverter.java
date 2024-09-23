package com.ilyaDudnikov.CurrencyExchange.converters;

import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeRateDto;
import com.ilyaDudnikov.CurrencyExchange.models.ExchangeRate;

public class ExchangeRateConverter {
    public static ExchangeRate convertToEntity(ExchangeRateDto exchangeRateDto) {
        return ExchangeRate.builder()
                .id(exchangeRateDto.getId())
                .baseCurrency(CurrencyConverter.convertToEntity(exchangeRateDto.getBaseCurrency()))
                .targetCurrency(CurrencyConverter.convertToEntity(exchangeRateDto.getTargetCurrency()))
                .rate(exchangeRateDto.getRate())
                .build();
    }

    public static ExchangeRateDto convertToDto(ExchangeRate exchangeRate) {
        return ExchangeRateDto.builder()
                .id(exchangeRate.getId())
                .baseCurrency(CurrencyConverter.convertToDto(exchangeRate.getBaseCurrency()))
                .targetCurrency(CurrencyConverter.convertToDto(exchangeRate.getTargetCurrency()))
                .rate(exchangeRate.getRate())
                .build();
    }
}
