package com.tcs.edu.decorator;

/**
 * The {@code SeverityDecorator} class contains a method that allows to match the severity level and its string value
 * and returns string value of chosen severity level.
 *
 * @author Anton Bezrukov
 */
public class SeverityDecorator {

    /**
     * Returns a string value of severity level.
     *
     * @param severity one of severity enum levels.
     */
    public static String severityMapToString(Severity severity){
        String severityString = null;

        switch (severity){
            case MINOR:
                severityString = "()";
                break;
            case REGULAR:
                severityString = "(!)";
                break;
            case MAJOR:
                severityString = "(!!!)";
                break;
        }

        return severityString;
    }
}
