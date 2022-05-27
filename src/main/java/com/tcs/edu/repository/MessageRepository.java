package com.tcs.edu.repository;

import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Severity;
import java.util.Collection;
import java.util.UUID;

public interface MessageRepository {

    UUID create(Message message);

    Message findById(UUID key);

    Collection<Message> findBySeverity(Severity severity);

    Collection<Message> findByBody(String body);

    Collection<Message> findBySeverityAndBody(Severity severity, String body);

    Collection<Message> findAll();

    Message update(Message updatedMessage);

    Message delete(UUID id);
}
