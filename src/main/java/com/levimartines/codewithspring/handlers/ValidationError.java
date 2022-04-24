package com.levimartines.codewithspring.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationError extends StandardError {

    @JsonProperty("errors")
    private Map<String, String> errors = new HashMap<>();

    public ValidationError(Long timestamp, Integer status, String error, String message,
                           String path) {
        super(timestamp, status, error, message, path);
    }

    public void putError(String fieldName, String message) {
        errors.put(fieldName, message);
    }

}
