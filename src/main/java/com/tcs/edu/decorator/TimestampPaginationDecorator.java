package com.tcs.edu.decorator;

/**
 * The {@code TimestampPaginationDecorator} class contains a method for decorating
 * a given string with the current timestamp, message count and page separator.
 *
 * @author Anton Bezrukov
 */
public class TimestampPaginationDecorator implements MessageDecorator{
    MessageDecorator timestampMessageDecorator = new TimestampMessageDecorator();
    MessageDecorator paginationDecorator = new PaginationDecorator();

    /**
     * Returns a string decorated with the current timestamp.
     *
     * @param message string to be decorated with the current timestamp, message count and page separator
     */
    public String decorate(String message){
        return paginationDecorator.decorate(timestampMessageDecorator.decorate(message));
    }
}
