package com.ilyaDudnikov.CurrencyExchange.dao;

import com.ilyaDudnikov.CurrencyExchange.exeptions.CurrencyException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.models.Currency;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {
    public List<Currency> getAll() {
        String sql = SqlQueries.SELECT_ALL_CURRENCIES;
        try (Connection conn = HikariCPDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Преобразование результата в список
            return convertResultSetToListModel(rs);
            
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private List<Currency> convertResultSetToListModel(ResultSet rs) throws SQLException {
        List<Currency> result = new LinkedList<>();
        while (rs.next()) {
            Currency currency = convertResultToModel(rs);
            result.add(currency);
        }
        return result;
    }

    private Currency convertResultToModel(ResultSet rs) throws SQLException {
        return Currency.builder()
                .id(rs.getLong("ID"))
                .code(rs.getString("Code"))
                .fullName(rs.getString("FullName"))
                .sign(rs.getString("Sign"))
                .build();
    }

    public Optional<Currency> getByCode(String code) {
        String sql = SqlQueries.SELECT_BY_CODE;
        try (Connection conn = HikariCPDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            Currency currency = null;
            if (rs.next())
                currency = Currency.builder()
                        .id(rs.getLong("ID"))
                        .code(rs.getString("Code"))
                        .fullName(rs.getString("FullName"))
                        .sign(rs.getString("Sign"))
                        .build();
            rs.close();

            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public Currency save(Currency currency) {
        String sql = SqlQueries.INSERT_CURRENCY;
        try (Connection conn = HikariCPDataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, currency.getCode());
            ps.setString(2, currency.getFullName());
            ps.setString(3, currency.getSign());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    currency.setId(generatedKeys.getLong(1));
                    return currency;
                } else {
                    throw new SQLException("Creating currency failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new CurrencyException(e);
            } else {
                throw new DatabaseException(e);
            }
        }
    }
}
