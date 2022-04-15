package com.tcs.edu.service;

import com.tcs.edu.decorator.Severity;
import com.tcs.edu.printer.ConsolePrinter;
import static com.tcs.edu.decorator.PaginationDecorator.paginate;
import static com.tcs.edu.decorator.SeverityDecorator.severityMapToString;
import static com.tcs.edu.decorator.TimestampMessageDecorator.decorate;

/**
 * The {@code MessageService} class contains a method for composing and printing of some message sting.
 *
 * @author Anton Bezrukov
 */
public class MessageService {

    /**
     * Prints a decorated message string. Given message string decorates with the timestamp,
     * severity level string value, message counter.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages  separated by "---" separator.
     *
     * @param message string to be decorated with the current timestamp, severity level, message counter.
     * @param messages varargs of strings to be decorated with the current timestamp, severity level, message counter.
     * @param level one of severity enum levels.
     */
    public static void process( Severity level, String message, String... messages){

        ConsolePrinter.print(
                paginate(decorate(message) + " " + severityMapToString(level)));

        for (String mess: messages){
            ConsolePrinter.print(
                    paginate(decorate(mess) + " " + severityMapToString(level)));
        }
    }
}
