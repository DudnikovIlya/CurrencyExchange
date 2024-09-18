package com.ilyaDudnikov.CurrencyExchange.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private long id;
    private String code;
    private String fullName;
    private String sign;
}
