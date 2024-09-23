package com.ilyaDudnikov.CurrencyExchange.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    private long id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;
}
