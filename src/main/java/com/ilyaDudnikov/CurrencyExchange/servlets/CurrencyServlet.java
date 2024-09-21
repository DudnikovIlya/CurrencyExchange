package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import com.ilyaDudnikov.CurrencyExchange.exeptions.CurrencyException;
import com.ilyaDudnikov.CurrencyExchange.services.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService service;
    private JsonWriter writer;
    @Override
    public void init(ServletConfig config) {
        service = new CurrencyService();
        writer = new JsonWriter();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = getCode(req);
            CurrencyDto currency = service.getByCode(code);
            writer.writeToResponse(resp, currency, HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            // Обработка исключения и отправка ошибки клиенту
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    private String getCode(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        String code = pathInfo.substring(1);

        if (CurrencyValidator.isValidCurrencyCode(code)) {
            return code;
        } else {
            throw new IllegalArgumentException("Invalid currency code: " + code);
        }
    }
}
