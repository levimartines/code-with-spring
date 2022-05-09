package com.levimartines.codewithspring.exceptions;

public class KafkaException extends RuntimeException {
    public KafkaException(String message) {
        super(message);
    }
}
