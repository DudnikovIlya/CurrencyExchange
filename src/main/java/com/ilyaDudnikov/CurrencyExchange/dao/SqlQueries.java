package com.ilyaDudnikov.CurrencyExchange.dao;

public class SqlQueries {
    public static final String SELECT_ALL_CURRENCIES = "SELECT * FROM Currencies";
    public static final String SELECT_BY_CODE = "SELECT * FROM Currencies WHERE Code = ?";
    public static final String INSERT_CURRENCY = "INSERT INTO Currencies (Code, FullName, Sign) values (?, ?, ?)";
}
