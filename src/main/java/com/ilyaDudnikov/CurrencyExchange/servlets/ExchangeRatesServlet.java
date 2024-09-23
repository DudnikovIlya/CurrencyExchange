package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeRateDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.CurrencyException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.ExchangeRateException;
import com.ilyaDudnikov.CurrencyExchange.services.ExchangeRateService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ExchangeRateService service;
    private JsonWriter writer;
    @Override
    public void init(ServletConfig config) {
        service = new ExchangeRateService();
        writer = new JsonWriter();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<ExchangeRateDto> exchangeRates = service.getAll();
            writer.writeToResponse(resp, exchangeRates, HttpServletResponse.SC_OK);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ExchangeRateDto exchangeRateDto = getExchangeRateDto(req);
            ExchangeRateDto createdExchangeRate = service.create(exchangeRateDto);
            writer.writeToResponse(resp, createdExchangeRate, HttpServletResponse.SC_CREATED);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (CurrencyException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ExchangeRateDto getExchangeRateDto(HttpServletRequest req) {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateString = req.getParameter("rate");

        if (CurrencyValidator.isValidCurrencyCode(baseCurrencyCode) &&
        CurrencyValidator.isValidCurrencyCode(targetCurrencyCode) &&
        CurrencyValidator.isValidBigDecimal(rateString)) {
            BigDecimal rate = new BigDecimal(rateString);
            CurrencyDto baseCurrency = CurrencyDto.builder().code(baseCurrencyCode).build();
            CurrencyDto targetCurrency = CurrencyDto.builder().code(targetCurrencyCode).build();

            return ExchangeRateDto.builder()
                    .baseCurrency(baseCurrency)
                    .targetCurrency(targetCurrency)
                    .rate(rate)
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid form fields: {baseCurrencyCode: " + baseCurrencyCode + "} " +
                    "{targetCurrencyCode: " + targetCurrencyCode + "} " +
                    "{rate: " + rateString + "}");
        }
    }
}
