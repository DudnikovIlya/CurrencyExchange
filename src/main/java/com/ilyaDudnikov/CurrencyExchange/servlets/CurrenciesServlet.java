package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dao.CurrencyDao;
import com.ilyaDudnikov.CurrencyExchange.dto.CurrencyDto;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private JsonWriter writer;
    private CurrencyDao currencyDao;
    @Override
    public void init(ServletConfig config) throws ServletException {
        writer = new JsonWriter();
        currencyDao = (CurrencyDao) config.getServletContext().getAttribute("currencyDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CurrencyDto> currencies = currencyDao.getAllCurrencies();
        writer.writeToResponse(resp, currencies, 200);
//        PrintWriter pw = resp.getWriter();
//        pw.println("Hello world!");
//        pw.close();
    }
}
