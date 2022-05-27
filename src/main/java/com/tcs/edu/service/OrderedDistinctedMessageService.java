package com.tcs.edu.service;

import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Severity;
import com.tcs.edu.repository.MessageRepository;
import java.util.*;


public class OrderedDistinctedMessageService extends ValidatedService {

    private final MessageRepository messageRepository;

    public OrderedDistinctedMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public UUID create(Message message) {
        return messageRepository.create(message);
    }

    @Override
    public Message findById(UUID key) {
        return messageRepository.findById(key);
    }

    @Override
    public Collection<Message> get(Severity severity) {
        return messageRepository.findBySeverity(severity);
    }

    @Override
    public Collection<Message> get(String body) {
        return messageRepository.findByBody(body);
    }

    @Override
    public Collection<Message> get(Severity severity, String body) {
        try {
            isArgsValid(severity, body);
        } catch (IllegalArgumentException e) {
            throw new ProcessException("Message getting goes wrong...", e);
        }
        return messageRepository.findBySeverityAndBody(severity, body);
    }

    @Override
    public Collection<Message> get() {
        return messageRepository.findAll();
    }


    @Override
    public Message put(Message newMessage) {
        return messageRepository.update(newMessage);
    }

    @Override
    public Message delete(UUID id) {
        return messageRepository.delete(id);
    }

}

