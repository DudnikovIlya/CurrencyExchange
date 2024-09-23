package com.ilyaDudnikov.CurrencyExchange.dao;

import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.models.Currency;
import com.ilyaDudnikov.CurrencyExchange.models.ExchangeRate;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ExchangeRateDao {
    public List<ExchangeRate> getAll() {
        String sql = SqlQueries.SELECT_ALL_EXCHANGE_RATES;
        try (Connection conn = HikariCPDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Преобразование результата в список
            return convertResultSetToListModel(rs);

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private List<ExchangeRate> convertResultSetToListModel(ResultSet rs) throws SQLException {
        List<ExchangeRate> result = new LinkedList<>();
        while (rs.next()) {
            ExchangeRate exchangeRate = convertResultToModel(rs);
            result.add(exchangeRate);
        }
        return result;
    }

    private ExchangeRate convertResultToModel(ResultSet rs) throws SQLException {
        Currency baseCurrency = Currency.builder()
                .id(rs.getLong("BaseId"))
                .code(rs.getString("BaseCode"))
                .fullName(rs.getString("BaseFullName"))
                .sign(rs.getString("BaseSign"))
                .build();

        Currency targetCurrency = Currency.builder()
                .id(rs.getLong("TargetId"))
                .code(rs.getString("TargetCode"))
                .fullName(rs.getString("TargetFullName"))
                .sign(rs.getString("TargetSign"))
                .build();

        return ExchangeRate.builder()
                .id(rs.getLong("ID"))
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rs.getBigDecimal("Rate"))
                .build();
    }
}

