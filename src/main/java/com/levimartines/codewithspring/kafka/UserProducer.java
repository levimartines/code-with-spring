package com.levimartines.codewithspring.kafka;

import com.levimartines.codewithspring.entities.model.User;
import com.levimartines.codewithspring.exceptions.KafkaException;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

    @Value("${kafka.topic.user}")
    private String userTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(final User user) {
        String messageKey = UUID.randomUUID().toString();
        UserMessage message = UserMessage.builder()
            .key(messageKey)
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .build();
        log.info("Sending new message: {}, topic: {}", message, userTopic);
        try {
            kafkaTemplate.send(userTopic, messageKey, message).get();
        } catch (Exception e) {
            log.error("Error sending message to kafka topic: {}, message: {}", userTopic, message);
            throw new KafkaException(e.getMessage());
        }
    }
}
