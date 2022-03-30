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
     * Returns a string decorated with the message count and the current timestamp.
     * Side effect on global messageCount.
     * @param message string to be decorated with the current timestamp.
     */
    public static String decorate(String message){
        messageCount ++;
        var decoratedMessage = messageCount + " " + Instant.now() + " " + message;
        return decoratedMessage;
    }
}
