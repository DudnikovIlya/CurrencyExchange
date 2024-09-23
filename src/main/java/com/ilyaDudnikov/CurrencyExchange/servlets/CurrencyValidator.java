package com.ilyaDudnikov.CurrencyExchange.servlets;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyValidator {
    private static final String CURRENCY_CODE_PATTERN = "^[A-Z]{3}$";
    public static final String CURRENCY_CODES_PATTERN = "^[A-Z]{6}$";
    private static final String CURRENCY_NAME_PATTERN = "^[a-zA-Z\\s]{1,50}$";
    private static final String CURRENCY_SIGN_PATTERN = "^.{1,5}$";

    public static boolean isValidCurrencyCode(String code) {
        return validateWithPattern(CURRENCY_CODE_PATTERN, code);
    }
    public static boolean isValidCurrencyCodes(String codes) { return validateWithPattern(CURRENCY_CODES_PATTERN, codes); }

    public static boolean isValidCurrencyName(String name) {
        return validateWithPattern(CURRENCY_NAME_PATTERN, name);
    }

    public static boolean isValidCurrencySign(String sign) {
        return validateWithPattern(CURRENCY_SIGN_PATTERN, sign);
    }

    private static boolean validateWithPattern(String patternString, String value) {
        if (value == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(value);
        // Проверяем, соответствует ли строка регулярному выражению
        return matcher.matches();
    }

    public static boolean isValidBigDecimal(String value) {
        if (value == null)
            return false;

        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false; // строка не может быть преобразована в BigDecimal
        }
    }
}
