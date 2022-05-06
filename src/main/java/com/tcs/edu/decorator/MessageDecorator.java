package com.tcs.edu.decorator;

/**
 * Interface MessageDecorator defines decorating operation.
 */
public interface MessageDecorator {

    /**
     * Returns decorated message.
     *
     * @param messageToDecorate message to be decorated.
     */
    String decorate(String messageToDecorate);
}
