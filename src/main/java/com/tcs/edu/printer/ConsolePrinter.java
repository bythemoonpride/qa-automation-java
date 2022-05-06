
package com.tcs.edu.printer;

/**
 * The {@code ConsolePrinter} class contains single method for console output of passed value
 * @author Eugene Krivosheyev
 */
public class ConsolePrinter implements Printer {
    /**
     * Prints the passed string to the console.
     *
     * @param message string to be printed in console.
     */
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
