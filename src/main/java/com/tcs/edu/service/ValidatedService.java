package com.tcs.edu.service;

/**
 * This class provides a method to validate given arguments
 */
public abstract class ValidatedService implements MessageService {

    /**
     * Returns true if given arguments are not null.
     *
     * @param o varargs of arguments.
     * @throws IllegalArgumentException if the specified argument is null.
     */
    boolean isArgsValid(Object... o) {
        for (Object object : o) {
            if (object == null) {
                throw new IllegalArgumentException("One or more of given arguments are null");
            }
        }
        return true;
    }
}
