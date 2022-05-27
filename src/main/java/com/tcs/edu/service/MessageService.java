package com.tcs.edu.service;

import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Severity;
import java.util.Collection;
import java.util.UUID;

/**
 * Interface MessageService defines the message processing operations.
 */
public interface MessageService {

    UUID create(Message message);

    Message findById(UUID key);

    Collection<Message> get(Severity severity);

    Collection<Message> get(String body);

    Collection<Message> get(Severity severity, String body);

    Collection<Message> get();

    Message put(Message newMessage);

    Message delete(UUID id);
}
