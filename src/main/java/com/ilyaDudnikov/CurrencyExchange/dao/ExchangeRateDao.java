package com.ilyaDudnikov.CurrencyExchange.dao;

import com.ilyaDudnikov.CurrencyExchange.exeptions.CurrencyException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.ExchangeRateException;
import com.ilyaDudnikov.CurrencyExchange.models.Currency;
import com.ilyaDudnikov.CurrencyExchange.models.ExchangeRate;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public Optional<ExchangeRate> getByCodes(String baseCode, String targetCode) {
        String sql = SqlQueries.SELECT_EXCHANGE_RATE_BY_CODES;
        try (Connection conn = HikariCPDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, baseCode);
            ps.setString(2, targetCode);

            ResultSet rs = ps.executeQuery();
            ExchangeRate exchangeRate = null;
            if (rs.next()) {
                exchangeRate = convertResultToModel(rs);
            }
            rs.close();

            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public void save(ExchangeRate exchangeRate) {
        String sql = SqlQueries.INSERT_EXCHANGE_RATE;
        try (Connection conn = HikariCPDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, exchangeRate.getBaseCurrency().getCode());
            ps.setString(2, exchangeRate.getTargetCurrency().getCode());
            ps.setBigDecimal(3, exchangeRate.getRate());
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                throw new ExchangeRateException("Exchange rate with such a pair already exists");
            } else if (e.getMessage().contains("SQLITE_CONSTRAINT_TRIGGER")) {
                throw new CurrencyException("One (or both) currencies from the currency pair do not exist in the database");
            } else {
                throw new DatabaseException(e);
            }
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

