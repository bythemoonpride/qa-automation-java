package com.tcs.edu.domain;

import com.tcs.edu.project_enum.Severity;
import com.tcs.edu.service.ProcessException;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code Message} class represents structure of message to be processed in implementations of MessageService interface.
 *
 * @author Anton Bezrukov
 */
public class Message {

    /**
     * Unique message id
     */
    private UUID id = null;

    /**
     * Message severity
     */
    private final Severity severity;

    /**
     * Message body
     */
    private final String body;

    /**
     * Constructor
     *
     * @param severity message severity
     * @param body     string message body
     */
    public Message(Severity severity, String body) {
        try {
            isArgsValid(severity, body);
        } catch (IllegalArgumentException e) {
            throw new ProcessException("Message creating goes wrong...", e);
        }
        this.severity = severity;
        this.body = body;
    }

    /**
     * @return severity
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * @return string body
     */
    public String getBody() {
        return body;
    }

    /**
     * @return uuid id for message
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id = " + id +
                ", severity = " + severity +
                ", body = '" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return severity == message.severity && Objects.equals(body, message.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, body);
    }

    boolean isArgsValid(Object... o) {
        for (Object object : o) {
            if (object == null) {
                throw new IllegalArgumentException("One or more of given arguments are null");
            }
        }
        return true;
    }
}
