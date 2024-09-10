import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class DataLoader {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Map<String, Object>> currencies = objectMapper.readValue(
                    new File("currencies.json"),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            List<Map<String, Object>> exchangeRates = objectMapper.readValue(
                    new File("exchange_rates.json"),
                    new TypeReference<List<Map<String, Object>>>() {}
            );

            String url = "jdbc:sqlite:database.db";
            Connection connection = DriverManager.getConnection(url);
            Statement stmt = connection.createStatement();

            // Создание таблицы Currencies, если её не существует
            String createTableCurrenciesQuery = "CREATE TABLE IF NOT EXISTS Currencies (" +
                                                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                "Code TEXT NOT NULL UNIQUE," +
                                                "FullName TEXT NOT NULL," +
                                                "Sign TEXT NOT NULL)";
            stmt.execute(createTableCurrenciesQuery);

            // Создание таблицы ExchangeRates, если её не существует
            String createTableExchangeRatesQuery = "CREATE TABLE IF NOT EXISTS ExchangeRates (" +
                                                   "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                   "BaseCurrencyId INTEGER," +
                                                   "TargetCurrencyId INTEGER," +
                                                   "Rate REAL," +
                                                   "FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID)," +
                                                   "FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID))";
            stmt.execute(createTableExchangeRatesQuery);

            // Вставка в таблицу Currencies
            String currencyInsertQuery = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";
            PreparedStatement currencyStmt = connection.prepareStatement(currencyInsertQuery);
            for (Map<String, Object> currency: currencies) {
                currencyStmt.setString(1, (String) currency.get("Code"));
                currencyStmt.setString(2, (String) currency.get("FullName"));
                currencyStmt.setString(3, (String) currency.get("Sign"));
                currencyStmt.addBatch();;
            }
            currencyStmt.executeBatch();

            // Вставка в таблицу ExchangeRates
            String exchangeRatesQuery = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
            PreparedStatement exchangeRatesStmt = connection.prepareStatement(exchangeRatesQuery);
            for (Map<String, Object> exchangeRate: exchangeRates) {
                exchangeRatesStmt.setInt(1, (Integer) exchangeRate.get("BaseCurrencyId"));
                exchangeRatesStmt.setInt(2, (Integer) exchangeRate.get("TargetCurrencyId"));
                exchangeRatesStmt.setBigDecimal(3, new BigDecimal(exchangeRate.get("Rate").toString()));
                exchangeRatesStmt.addBatch();
            }
            exchangeRatesStmt.executeBatch();

            connection.close();

            System.out.println("Данные успешно загружены в базу данных!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
