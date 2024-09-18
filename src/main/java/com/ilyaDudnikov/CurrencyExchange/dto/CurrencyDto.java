package com.ilyaDudnikov.CurrencyExchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDto {
    private long id;
    private String code;
    private String fullName;
    private String sign;
}
