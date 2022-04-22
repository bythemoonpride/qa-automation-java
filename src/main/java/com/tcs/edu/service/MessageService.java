package com.tcs.edu.service;

import com.tcs.edu.decorator.Severity;
import com.tcs.edu.printer.ConsolePrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tcs.edu.decorator.PaginationDecorator.paginate;
import static com.tcs.edu.decorator.SeverityDecorator.severityToString;
import static com.tcs.edu.decorator.TimestampMessageDecorator.decorate;
import static java.lang.String.format;

/**
 * The {@code MessageService} class contains a method for composing and printing of some message sting.
 *
 * @author Anton Bezrukov
 */
public class MessageService {

    /**
     * Prints a decorated message string. Given message string decorates with the timestamp,
     * severity level string value, message counter.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages separated by "---" separator.
     * Null messages are skipped.
     *
     * @param message  string to be decorated with the current timestamp, severity level, message counter.
     * @param messages varargs of strings to be decorated with the current timestamp, severity level, message counter.
     * @param level    one of severity enum levels.
     */
    public static void process(Severity level, String message, String... messages) {

        List<String> allMessages = new ArrayList<>();

        if (messages != null) {
            Collections.addAll(allMessages, messages);
        }

        allMessages.add(0, message);

        for (String mess : allMessages) {
            if (mess != null) {
                ConsolePrinter.print(
                        paginate(format("%s %s", decorate(mess), severityToString(level))));
            }
        }
    }

}

