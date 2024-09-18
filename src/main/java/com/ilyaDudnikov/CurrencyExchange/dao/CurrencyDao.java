package com.ilyaDudnikov.CurrencyExchange.dao;

import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;

import java.io.File;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CurrencyDao {
    public List<CurrencyDto> getAllCurrencies() {
        String sql = SqlQueries.SELECT_ALL_CURRENCIES;
//        try (Connection conn = HikariCPDataSource.getConnection();
        try {
            Class.forName("org.sqlite.JDBC");
            String dbPath = "D:/Idea Projects/CurrencyExchange/src/main/resources/database.db";  // Укажи полный путь
            String url = "jdbc:sqlite:" + dbPath;
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute("SELECT 1");
            ResultSet rs = stmt.executeQuery("SELECT * FROM Currencies");
            // Преобразование результата в список
            return convertResultSetToListDto(rs);
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CurrencyDto> convertResultSetToListDto(ResultSet rs) throws SQLException {
        List<CurrencyDto> result = new LinkedList<>();
        while (rs.next()) {
            CurrencyDto currencyDto = convertResultToDto(rs);
            result.add(currencyDto);
        }
        return result;
    }

    private CurrencyDto convertResultToDto(ResultSet rs) throws SQLException {
        return CurrencyDto.builder()
                .id(rs.getLong("ID"))
                .code(rs.getString("Code"))
                .fullName(rs.getString("FullName"))
                .sign(rs.getString("Sign"))
                .build();
    }
}
