package com.tcs.edu.service;

import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Doubling;
import com.tcs.edu.project_enum.MessageOrder;

/**
 * Interface MessageService defines the message processing operations.
 */
public interface MessageService {


    /**
     * Processes given message.
     *
     * @param message  message to be processed.
     * @param messages varargs of messages to be processed.
     */
    void process(Message message, Message... messages);

    /**
     * Processes given message.
     *
     * @param message  message to be processed.
     * @param messages varargs of messages to be processed.
     * @param order    order of messages to be processed.
     */
    void process(MessageOrder order, Message message, Message... messages);

    /**
     * Processes given message.
     *
     * @param message  message to be processed.
     * @param messages varargs of messages to be processed.
     * @param order    order of messages to be processed.
     * @param doubling doubling strategy for messages to be processed
     */
    void process(MessageOrder order, Doubling doubling, Message message, Message... messages);
}
