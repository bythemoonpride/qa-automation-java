package com.tcs.edu.decorator;

import java.time.Instant;

/**
 * The {@code TimestampMessageDecorator} class contains a message count field and a method for decorating
 * a given string with the message count and the current timestamp.
 *
 * @author Anton Bezrukov
 */
public class TimestampMessageDecorator {
    /**
     * Counter of displayed messages.
     */
    private static int messageCount = 0;

    /**
     * The size of page into which decorated strings are divided
     */
    final static int PAGE_SIZE = 2;

    /**
     * Returns a string decorated with the message count and the current timestamp.
     * Side effect on global messageCount.
     *
     * @param message string to be decorated with the current timestamp.
     */
    public static String decorate(String message) {
        String messageTemplate = "%d %s %s";
        messageCount++;
        if (messageCount % PAGE_SIZE == 0) {
            messageTemplate += "\n---";
        }
        return String.format(messageTemplate, messageCount, Instant.now(), message);
    }
}
