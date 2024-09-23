package com.ilyaDudnikov.CurrencyExchange.dao;

public class SqlQueries {
    public static final String SELECT_ALL_CURRENCIES = "SELECT * FROM Currencies";
    public static final String SELECT_BY_CODE = "SELECT * FROM Currencies WHERE Code = ?";
    public static final String INSERT_CURRENCY = "INSERT INTO Currencies (Code, FullName, Sign) values (?, ?, ?)";

    public static final String SELECT_ALL_EXCHANGE_RATES = "SELECT er.ID, " +
            "base.ID BaseId, base.Code BaseCode, base.FullName BaseFullName, base.Sign BaseSign, " +
            "target.ID TargetId, target.Code TargetCode, target.FullName TargetFullName, target.Sign TargetSign, " +
            "er.Rate " +
            "FROM ExchangeRates er " +
            "JOIN Currencies base ON er.BaseCurrencyId = base.ID " +
            "JOIN Currencies target ON er.TargetCurrencyId = target.ID";

    public static final String SELECT_EXCHANGE_RATE_BY_CODES = "SELECT er.ID, " +
            "base.ID BaseId, base.Code BaseCode, base.FullName BaseFullName, base.Sign BaseSign, " +
            "target.ID TargetId, target.Code TargetCode, target.FullName TargetFullName, target.Sign TargetSign, " +
            "er.Rate " +
            "FROM ExchangeRates er " +
            "JOIN Currencies base ON er.BaseCurrencyId = base.ID " +
            "JOIN Currencies target ON er.TargetCurrencyId = target.ID " +
            "WHERE base.Code = ? AND target.Code = ?";

    public static final String INSERT_EXCHANGE_RATE = "INSERT OR ABORT INTO ExchangeRates " +
            "(BaseCurrencyId, TargetCurrencyId, Rate) values " +
            "((SELECT ID FROM Currencies WHERE Code = ?), " +
            "(SELECT id FROM Currencies WHERE Code = ?), " +
            "?)";
}
