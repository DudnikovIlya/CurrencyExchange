package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.ExchangeRateException;
import com.ilyaDudnikov.CurrencyExchange.services.ExchangeService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeService service;
    private JsonWriter writer;
    @Override
    public void init() {
        service = new ExchangeService();
        writer= new JsonWriter();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeDto exchangeDto = getExchangeDto(req);
            ExchangeDto resultExchange = service.exchange(exchangeDto);
            writer.writeToResponse(resp, resultExchange, HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ExchangeDto getExchangeDto(HttpServletRequest req) {
        String baseCode = req.getParameter("from");
        String targetCode = req.getParameter("to");
        String amountString = req.getParameter("amount");

        if(CurrencyValidator.isValidCurrencyCode(baseCode) &&
                CurrencyValidator.isValidCurrencyCode(targetCode) &&
                CurrencyValidator.isValidBigDecimal(amountString)) {

            BigDecimal amount = new BigDecimal(amountString);
            CurrencyDto baseCurrency = CurrencyDto.builder().code(baseCode).build();
            CurrencyDto targetCurrency = CurrencyDto.builder().code(targetCode).build();

            return ExchangeDto.builder()
                    .baseCurrency(baseCurrency)
                    .targetCurrency(targetCurrency)
                    .amount(amount)
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid form fields: " +
                    "{baseCode: " + baseCode + "}, " +
                    "{targetCode: " + targetCode + "}, " +
                    "{amount: " + amountString + "}");
        }
        
    }
}
