package com.tcs.edu.decorator;

import java.time.Instant;

/**
 * The {@code TimestampMessageDecorator} class contains single method for decorating given string
 * with the current timestamp.
 *
 * @author Anton Bezrukov
 */
public class TimestampMessageDecorator {

    /**
     * Returns a string decorated with the current timestamp.
     *
     * @param message string to be decorated with the current timestamp.
     */
    public static String decorate(String message){
        String decoratedMessage;
        decoratedMessage = Instant.now() + " " + message;
        return decoratedMessage;
    }
}
