package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.ilyaDudnikov.CurrencyExchange.dao.CurrencyDao;
import com.ilyaDudnikov.CurrencyExchange.dao.HikariCPDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        CurrencyDao currencyDao = new CurrencyDao();
        context.setAttribute("currencyDao", currencyDao);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HikariCPDataSource.close();
    }
}
