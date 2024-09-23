package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dto.ExchangeRateDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.ExchangeRateException;
import com.ilyaDudnikov.CurrencyExchange.services.ExchangeRateService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService service;
    private JsonWriter writer;
    @Override
    public void init() {
        service = new ExchangeRateService();
        writer = new JsonWriter();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String[] codes = getCodes(req);
            ExchangeRateDto exchangeRate = service.getByCodes(codes[0], codes[1]);
            writer.writeToResponse(resp, exchangeRate, HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (ExchangeRateException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private String[] getCodes(HttpServletRequest req) {
        String urlCodes = req.getPathInfo().substring(1);
        if (CurrencyValidator.isValidCurrencyCodes(urlCodes)) {
            String baseCode = urlCodes.substring(0, 3);
            String targetCode = urlCodes.substring(3);
            return new String[] {baseCode, targetCode};
        } else {
            throw new IllegalArgumentException("Invalid currency codes: " + urlCodes);
        }
    }
}
