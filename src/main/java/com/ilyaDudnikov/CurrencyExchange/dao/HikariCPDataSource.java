package com.ilyaDudnikov.CurrencyExchange.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class HikariCPDataSource {

    private static final String jdbcUrl = "jdbc:sqlite:resources:database.db";
    private static final int maxPoolSize = 10;

    private static final HikariDataSource dataSource;
    static {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl(jdbcUrl);
        config.setMaximumPoolSize(maxPoolSize);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        dataSource.close();
    }
}
