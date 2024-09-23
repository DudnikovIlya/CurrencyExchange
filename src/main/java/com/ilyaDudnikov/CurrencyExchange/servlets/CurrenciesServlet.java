package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.CurrencyException;
import com.ilyaDudnikov.CurrencyExchange.exeptions.DatabaseException;
import com.ilyaDudnikov.CurrencyExchange.services.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private JsonWriter writer;
    private CurrencyService service;
    @Override
    public void init(ServletConfig config) {
        writer = new JsonWriter();
        service = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<CurrencyDto> currencies = service.getAll();
            writer.writeToResponse(resp, currencies, HttpServletResponse.SC_OK);
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            CurrencyDto currencyDto = getCurrencyDto(req);
            CurrencyDto createdCurrency = service.create(currencyDto);
            writer.writeToResponse(resp, createdCurrency, HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyException e) {
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage());
        } catch (DatabaseException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }

    private CurrencyDto getCurrencyDto(HttpServletRequest req) {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (CurrencyValidator.isValidCurrencyName(name)
                && CurrencyValidator.isValidCurrencyCode(code)
                && CurrencyValidator.isValidCurrencySign(sign)) {
            return CurrencyDto.builder()
                    .fullName(name)
                    .code(code)
                    .sign(sign)
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid form fields");
        }

    }
}
