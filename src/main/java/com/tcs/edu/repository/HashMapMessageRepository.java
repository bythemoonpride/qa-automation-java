package com.tcs.edu.repository;

import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Severity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class HashMapMessageRepository implements MessageRepository {

    private Map<UUID, Message> messages = new HashMap<>();

    public UUID create(Message message) {
        UUID messageId = UUID.randomUUID();
        message.setId(messageId);
        messages.put(messageId, message);
        return messageId;
    }

    public Message findById(UUID key) {
        return messages.get(key);
    }

    public Collection<Message> findBySeverity(Severity severity) {
        return messages.values().stream()
                .filter(message -> message.getSeverity() == severity)
                .collect(Collectors.toList());
    }

    public Collection<Message> findByBody(String body) {
        return messages.values().stream()
                .filter(message -> message.getBody().equals(body))
                .collect(Collectors.toList());
    }

    public Collection<Message> findBySeverityAndBody(Severity severity, String body) {
        return messages.values().stream()
                .filter(message -> message.getSeverity() == severity)
                .filter(message -> message.getBody().equals(body))
                .collect(Collectors.toList());
    }

    public Collection<Message> findAll() {
        return messages.values();
    }


    public Message update(Message updatedMessage) {
        return messages.replace(updatedMessage.getId(), updatedMessage);
    }

    public Message delete(UUID id) {
        return messages.remove(id);
    }

}
