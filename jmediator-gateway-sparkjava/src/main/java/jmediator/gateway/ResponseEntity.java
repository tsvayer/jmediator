package jmediator.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseEntity<T> {

    public static <T> String ok(T body, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
