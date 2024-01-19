package com.booking.arena.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CustomRequestUrl {
    public static <T> T getToken(String username, String password, String url, Class<T> type) {
        String jsonLoginData = String.format("""
                {
                  "username": "%s",
                  "password": "%s"
                }
                """, username, password);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonLoginData))
                .build();
        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), type);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
