package com.ilyaDudnikov.CurrencyExchange.services;

import com.ilyaDudnikov.CurrencyExchange.converters.CurrencyConverter;
import com.ilyaDudnikov.CurrencyExchange.dao.CurrencyDao;
import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.CurrencyException;
import com.ilyaDudnikov.CurrencyExchange.models.Currency;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurrencyService {
    private final CurrencyDao currencyDao;

    public CurrencyService() {
        currencyDao = new CurrencyDao();
    }

    public List<CurrencyDto> getAll() {
        List<Currency> currencies = currencyDao.getAll();
        return currencies.stream()
                .map(CurrencyConverter::convertToDto)
                .collect(Collectors.toList());
    }

    public CurrencyDto getByCode(String code) {
        Optional<Currency> currency = currencyDao.getByCode(code);
        if (currency.isPresent()) {
            return CurrencyConverter.convertToDto(currency.get());
        } else {
            throw new CurrencyException("Currency with this code not found: " + code);
        }
    }

    public CurrencyDto create(CurrencyDto currencyDto) {
        Currency currency = CurrencyConverter.convertToEntity(currencyDto);
        Currency createdCurrency = currencyDao.save(currency);
        return CurrencyConverter.convertToDto(createdCurrency);
    }
}
