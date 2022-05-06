package com.tcs.edu.service;

import com.tcs.edu.project_enum.Doubling;
import com.tcs.edu.project_enum.MessageOrder;
import com.tcs.edu.project_enum.Severity;
import com.tcs.edu.printer.ConsolePrinter;

import java.util.*;

import static com.tcs.edu.decorator.PaginationDecorator.paginate;
import static com.tcs.edu.decorator.SeverityDecorator.severityToString;
import static com.tcs.edu.decorator.TimestampMessageDecorator.decorate;
import static com.tcs.edu.project_enum.Doubling.*;
import static com.tcs.edu.project_enum.MessageOrder.*;
import static java.lang.String.format;

/**
 * The {@code MessageService} class contains a method for composing and printing of some message sting.
 *
 * @author Anton Bezrukov
 */
public class MessageService {
    private static final MessageOrder DEFAULT_ORDER = ASC;
    private static final Doubling DEFAULT_DOUBLING = DOUBLES;

    /**
     * Prints a decorated message string. Given message string decorates with the timestamp,
     * severity level string value, message counter.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages separated by "---" separator.
     * Null messages are skipped.
     *
     * @param message  string to be decorated with the current timestamp, severity level, message counter.
     * @param messages varargs of strings to be decorated with the current timestamp, severity level, message counter.
     * @param level    one of severity enum levels.
     * @see #process(Severity, MessageOrder, String, String...)
     */
    public static void process(Severity level, String message, String... messages) {
        process(level, DEFAULT_ORDER, message, messages);
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
     * @see #process(Severity, MessageOrder, Doubling, String, String...)
     */
    public static void process(Severity level, MessageOrder order, String message, String... messages) {
        process(level, order, DEFAULT_DOUBLING, message, messages);
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
     * @param doubling one of doubling enums.
     */
    public static void process(
            Severity level, MessageOrder order, Doubling doubling, String message, String... messages) {

        List<String> allMessages = new ArrayList<>();
        allMessages.add(message);

        if (messages != null) {
            Collections.addAll(allMessages, messages);
        }

        List<String> processedMessages = getDoublingProcessedMessages(doubling, getOrderedMessages(order, allMessages));

        for (String mess : processedMessages) {
            if (mess != null) {
                ConsolePrinter.print(paginate(format("%s %s", decorate(mess), severityToString(level))));
            }
        }
    }


    /**
     * Returns a list of messages in chosen order.
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
            messageList = Arrays.asList(orderedArray);
        }
        return messageList;
    }

    /**
     * Returns a list of messages in chosen doubling strategy
     *
     * @param doubling    one of doubling enums.
     * @param messageList list of messages to be doubling processed.
     */
    private static List<String> getDoublingProcessedMessages(Doubling doubling, List<String> messageList) {
        if (doubling == DISTINCT) {
            String[] distinctArray = new String[messageList.size()];
            boolean isDoubling = false;
            int i = 0;
            for (String mess : messageList) {
                for (String distMess : distinctArray) {
                    if (Objects.equals(distMess, mess)) {
                        isDoubling = true;
                        break;
                    }
                }
                if (!isDoubling) {
                    distinctArray[i] = mess;
                    i++;
                }
                isDoubling = false;
            }
            messageList = Arrays.asList(distinctArray);
        }
        return messageList;
    }


}

