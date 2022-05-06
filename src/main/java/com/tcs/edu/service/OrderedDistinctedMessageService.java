package com.tcs.edu.service;

import com.tcs.edu.decorator.MessageDecorator;
import com.tcs.edu.printer.Printer;
import com.tcs.edu.domain.Message;
import com.tcs.edu.project_enum.Doubling;
import com.tcs.edu.project_enum.MessageOrder;

import java.util.*;

import static com.tcs.edu.project_enum.Doubling.*;
import static com.tcs.edu.project_enum.MessageOrder.*;
import static java.lang.String.format;

/**
 * The {@code OrderedDistinctedMessageService} class contains a methods for processing some messages.
 * The message must be decorated, sorted, processed according to the doubling strategy.
 *
 * @author Anton Bezrukov
 */
public class OrderedDistinctedMessageService implements MessageService {
    private static final MessageOrder DEFAULT_ORDER = ASC;
    private static final Doubling DEFAULT_DOUBLING = DOUBLES;

    /**
     * Message decorator
     */
    private final MessageDecorator messageDecorator;

    /**
     * Message printer
     */
    private final Printer printer;


    /**
     * Constructor
     *
     * @param messageDecorator decorator to be used to decorate message
     * @param printer          to be used to print messages
     */
    public OrderedDistinctedMessageService(MessageDecorator messageDecorator, Printer printer) {
        this.messageDecorator = messageDecorator;
        this.printer = printer;
    }

    /**
     * Prints a decorated message string. Given message decorates with with chosen decorator.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages separated by "---" separator.
     * Null messages are skipped.
     *
     * @param message  message to be decorated with with chosen decorator.
     * @param messages varargs of messages to be decorated with with chosen decorator.
     * @see #process(MessageOrder, Message, Message...)
     */
    public void process(Message message, Message... messages) {
        process(DEFAULT_ORDER, message, messages);
    }

    /**
     * Prints a decorated message strings. Given message decorates with chosen decorator.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages separated by "---" separator.
     * Null messages are skipped.
     * It is possible to specify the order of the messages.
     *
     * @param message  message to be decorated with with chosen decorator.
     * @param messages varargs of messages to be decorated with with chosen decorator.
     * @param order    one of order enums, defines order of given messages.
     * @see #process(MessageOrder, Doubling, Message, Message...)
     */
    public void process(MessageOrder order, Message message, Message... messages) {
        process(order, DEFAULT_DOUBLING, message, messages);
    }

    /**
     * Prints a decorated message strings. Given message decorates with chosen decorator.
     * Printed rows are collected in pages of PaginationDecoration.PAGE_SIZE, pages separated by "---" separator.
     * Null messages are skipped.
     * It is possible to specify the order of the messages.
     *
     * @param message  message to be decorated with with chosen decorator.
     * @param messages varargs of messages to be decorated with with chosen decorator.
     * @param order    one of order enums, defines order of given messages.
     * @param doubling one of doubling enums, defines doubling strategy for given messages.
     */
    public void process(MessageOrder order, Doubling doubling, Message message, Message... messages) {

        List<Message> allMessages = new ArrayList<>();
        allMessages.add(message);

        if (messages != null) {
            Collections.addAll(allMessages, messages);
        }

        List<Message> processedMessages = getDoublingProcessedMessages(doubling, getOrderedMessages(order, allMessages));

        for (Message mess : processedMessages) {
            if (mess != null && mess.getBody() != null) {
                printer.print(messageDecorator.decorate(format("%s %s", mess.getBody(), mess.getSeverity().getLevel())));
            }
        }
    }


    /**
     * Returns a list of messages in chosen order.
     *
     * @param order       one of order enums.
     * @param messageList list of messages to be sorted in order
     */
    private static List<Message> getOrderedMessages(MessageOrder order, List<Message> messageList) {
        if (order == MessageOrder.DESC) {
            Message[] orderedArray = new Message[messageList.size()];
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
    private static List<Message> getDoublingProcessedMessages(Doubling doubling, List<Message> messageList) {
        if (doubling == DISTINCT) {
            Message[] distinctArray = new Message[messageList.size()];
            boolean isDoubling = false;
            int i = 0;
            for (Message mess : messageList) {
                for (Message distMess : distinctArray) {
                    if (distMess != null && mess != null && Objects.equals(distMess.getBody(), mess.getBody())) {
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

