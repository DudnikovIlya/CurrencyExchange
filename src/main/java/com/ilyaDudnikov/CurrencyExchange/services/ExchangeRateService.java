package com.ilyaDudnikov.CurrencyExchange.services;

import com.ilyaDudnikov.CurrencyExchange.converters.ExchangeRateConverter;
import com.ilyaDudnikov.CurrencyExchange.dao.ExchangeRateDao;
import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeRateDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.ExchangeRateException;
import com.ilyaDudnikov.CurrencyExchange.models.ExchangeRate;

import java.util.List;
import java.util.Optional;

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

    public ExchangeRateDto getByCodes(String baseCode, String targetCode) {
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByCodes(baseCode, targetCode);
        if (exchangeRate.isPresent()) {
            return ExchangeRateConverter.convertToDto(exchangeRate.get());
        } else {
            throw new ExchangeRateException("Exchange rate with this codes not found: " +
                    "{baseCode: " + baseCode + "}, {targetCode: " + targetCode + "}");
        }
    }

    public ExchangeRateDto create(ExchangeRateDto exchangeRateDto) {
        ExchangeRate exchangeRate = ExchangeRateConverter.convertToEntity(exchangeRateDto);
        exchangeRateDao.save(exchangeRate);
        Optional<ExchangeRate> createdExchangeRate = exchangeRateDao.getByCodes(
                exchangeRateDto.getBaseCurrency().getCode(),
                exchangeRateDto.getTargetCurrency().getCode());
        return ExchangeRateConverter.convertToDto(createdExchangeRate.orElse(new ExchangeRate()));

    }
}
