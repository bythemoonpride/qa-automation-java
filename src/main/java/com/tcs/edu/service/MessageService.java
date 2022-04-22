package com.tcs.edu.service;

import com.tcs.edu.project_enum.MessageOrder;
import com.tcs.edu.project_enum.Severity;
import com.tcs.edu.printer.ConsolePrinter;

import java.util.ArrayList;
import java.util.Arrays;
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
        allMessages.add(message);
        if (messages != null) {
            Collections.addAll(allMessages, messages);
        }

        for (String mess : allMessages) {
            if (mess != null) {
                ConsolePrinter.print(paginate(format("%s %s", decorate(mess), severityToString(level))));
            }
        }
    }

    /**
     * Prints a decorated message strings. Given message strings decorates with the timestamp,
     * severity level string value, message counter.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages separated by "---" separator.
     * Null messages are skipped.
     * It is possible to specify the order of the messages.
     *
     * @param message  string to be decorated with the current timestamp, severity level, message counter.
     * @param messages varargs of strings to be decorated with the current timestamp, severity level, message counter.
     * @param level    one of severity enum levels.
     * @param order    one of order enums.
     */
    public static void process(Severity level, MessageOrder order, String message, String... messages) {

        List<String> allMessages = new ArrayList<>();
        allMessages.add(message);

        if (messages != null) {
            Collections.addAll(allMessages, messages);
        }

        List<String> orderedMessages = getOrderedMessages(order, allMessages);

        for (String mess : orderedMessages) {
            if (mess != null) {

                ConsolePrinter.print(paginate(format("%s %s", decorate(mess), severityToString(level))));
            }
        }
    }


    /**
     * Returns a string array of values in reverse order.
     *
     * @param order       one of order enums.
     * @param messageList list of messages to be sorted in order
     */
    private static List<String> getOrderedMessages(MessageOrder order, List<String> messageList) {
        if (order == MessageOrder.DESC) {
            String[] orderedArray = new String[messageList.size()];
            for (int i = messageList.size() - 1; i >= 0; i--) {
                orderedArray[i] = messageList.get(messageList.size() - 1 - i);
            }
            messageList = new ArrayList<>(Arrays.asList(orderedArray));
        }
        return messageList;
    }


}

