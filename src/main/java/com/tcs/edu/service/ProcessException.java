package com.tcs.edu.service;

/**
 * Unchecked exception,thrown to indicate that an message processing has failed.
 */
public class ProcessException extends RuntimeException {

    public ProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
