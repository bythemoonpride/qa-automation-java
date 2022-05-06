package com.tcs.edu.domain;

import com.tcs.edu.project_enum.Severity;

/**
 * The {@code Message} class represents structure of message to be processed in implementations of MessageService interface.
 * @author Anton Bezrukov
 */
public class Message {
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
     * @param severity message severity
     * @param body string message body
     */
    public Message(Severity severity, String body) {
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
}
