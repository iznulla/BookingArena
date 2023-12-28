package com.booking.arena.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class DeserializeJson {
    public static <T> T get(String object, Class<T> type) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.readValue(object, type);
    }

}
