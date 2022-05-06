package com.tcs.edu.decorator;

/**
 * The {@code PaginationDecorator} class contains a message count field, constant for a page size and
 * a method for decorating
 * a given string with the message count and the page separator.
 *
 * @author Anton Bezrukov
 */
public class PaginationDecorator implements MessageDecorator {
    /**
     * Counter of displayed messages.
     */
    private static int messageCount = 0;

    /**
     * The size of page into which decorated strings are divided
     */
    final static int PAGE_SIZE = 2;

    /**
     * Returns a string decorated with the message count and the page separator.
     * The separator is added to the line whose number is divisible by PAGE_SIZE.
     * Side effect on global messageCount.
     *
     * @param message string to be decorated with the current timestamp and the separator.
     */
    public String decorate(String message) {
        String messageTemplate = "%d %s";
        messageCount++;
        if (messageCount % PAGE_SIZE == 0) {
            messageTemplate += "\n---";
        }
        return String.format(messageTemplate, messageCount, message);
    }
}
