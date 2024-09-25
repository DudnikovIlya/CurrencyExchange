package com.ilyaDudnikov.CurrencyExchange.services;

import com.ilyaDudnikov.CurrencyExchange.converters.CurrencyConverter;
import com.ilyaDudnikov.CurrencyExchange.dao.ExchangeRateDao;
import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.ExchangeRateException;
import com.ilyaDudnikov.CurrencyExchange.models.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private final ExchangeRateDao exchangeRateDao;
    private static final String USD_CODE = "USD";

    public ExchangeService() {
        exchangeRateDao = new ExchangeRateDao();
    }
    public ExchangeDto exchange(ExchangeDto exchangeDto) {
        String baseCode = exchangeDto.getBaseCurrency().getCode();
        String targetCode = exchangeDto.getTargetCurrency().getCode();
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByCodes(baseCode, targetCode);

        if (exchangeRate.isEmpty()) {
            exchangeRate = getByReverseRate(baseCode, targetCode);
        }
        if (exchangeRate.isEmpty()) {
            exchangeRate = getTransitive(baseCode, targetCode);
        }

        if (exchangeRate.isPresent()) {
            CurrencyDto baseCurrency = CurrencyConverter.convertToDto(exchangeRate.get().getBaseCurrency());
            CurrencyDto targetCurrency = CurrencyConverter.convertToDto(exchangeRate.get().getTargetCurrency());
            BigDecimal rate = exchangeRate.get().getRate();
            BigDecimal amount = exchangeDto.getAmount();
            BigDecimal convertedAmount = rate.multiply(amount);

            return new ExchangeDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
        } else {
            throw new ExchangeRateException("The required exchange rates for these currencies are not available in the database: " +
                    "{baseCode: " + baseCode + "}, " +
                    "{targetCode: " + targetCode + "}");
        }


    }

    private Optional<ExchangeRate> getByReverseRate(String baseCode, String targetCode) {
        Optional<ExchangeRate> reverseExchangeRate = exchangeRateDao.getByCodes(targetCode, baseCode);
        if (reverseExchangeRate.isEmpty()) {
            return Optional.empty();
        }

        ExchangeRate result = reverseExchangeRate.get();
        return Optional.of(new ExchangeRate(
                result.getId(),
                result.getTargetCurrency(),
                result.getBaseCurrency(),
                BigDecimal.ONE.divide(result.getRate(), 4, RoundingMode.HALF_UP)));
    }

    private Optional<ExchangeRate> getTransitive(String baseCode, String targetCode) {
        Optional<ExchangeRate> baseUsdOptional = exchangeRateDao.getByCodes(baseCode, USD_CODE);
        Optional<ExchangeRate> targetUsdOptional = exchangeRateDao.getByCodes(targetCode, USD_CODE);

        if (baseUsdOptional.isPresent() && targetUsdOptional.isPresent()) {
            ExchangeRate baseUsd = baseUsdOptional.get();
            ExchangeRate targetUsd = targetUsdOptional.get();

            ExchangeRate result = ExchangeRate.builder()
                    .baseCurrency(baseUsd.getBaseCurrency())
                    .targetCurrency(targetUsd.getBaseCurrency())
                    .rate(targetUsd.getRate().divide(baseUsd.getRate(), 4, RoundingMode.HALF_UP))
                    .build();
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }
}
