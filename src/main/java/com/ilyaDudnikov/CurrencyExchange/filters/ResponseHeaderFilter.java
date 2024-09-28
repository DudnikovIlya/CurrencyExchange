package com.ilyaDudnikov.CurrencyExchange.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter("/*")
public class ResponseHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;
        httpResp.setCharacterEncoding(StandardCharsets.UTF_8.name()); // UTF-8
        httpResp.setContentType(MediaType.APPLICATION_JSON); // application/json

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
