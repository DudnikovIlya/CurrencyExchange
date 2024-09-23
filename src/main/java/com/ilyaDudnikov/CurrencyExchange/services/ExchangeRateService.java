package com.ilyaDudnikov.CurrencyExchange.services;

import com.ilyaDudnikov.CurrencyExchange.converters.ExchangeRateConverter;
import com.ilyaDudnikov.CurrencyExchange.dao.ExchangeRateDao;
import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeRateDto;
import com.ilyaDudnikov.CurrencyExchange.models.ExchangeRate;

import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService() {
        exchangeRateDao = new ExchangeRateDao();
    }
    public List<ExchangeRateDto> getAll() {
        List<ExchangeRate> exchangeRates =  exchangeRateDao.getAll();
        return exchangeRates.stream()
                .map(ExchangeRateConverter::convertToDto)
                .toList();

    }
}
