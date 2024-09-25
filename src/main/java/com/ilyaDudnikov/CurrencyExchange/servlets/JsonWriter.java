package com.ilyaDudnikov.CurrencyExchange.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonWriter {
    private final ObjectMapper objectMapper;

    public JsonWriter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void writeToResponse(HttpServletResponse response, Object obj, int statusCode) throws IOException {
        response.setStatus(statusCode);

        String jsonStr = objectMapper.writeValueAsString(obj);
        response.getWriter().write(jsonStr);
    }
}
