package com.tcs.edu;

import com.tcs.edu.decorator.TimestampPaginationDecorator;
import com.tcs.edu.domain.Message;
import com.tcs.edu.printer.ConsolePrinter;
import com.tcs.edu.service.MessageService;
import com.tcs.edu.service.OrderedDistinctedMessageService;

import static com.tcs.edu.project_enum.Doubling.*;
import static com.tcs.edu.project_enum.MessageOrder.*;
import static com.tcs.edu.project_enum.Severity.*;

class Application {
    public static void main(String[] args) {

        MessageService messageService = new OrderedDistinctedMessageService(
                new TimestampPaginationDecorator(),
                new ConsolePrinter()
        );

        messageService.process(
                DESC,
                DISTINCT,
                new Message(REGULAR, "Hello0"),
                new Message(MAJOR, "Hello1"),
                new Message(MINOR, "Hello2"),
                new Message(MINOR, "Hello2"),
                new Message(REGULAR, "Hello2"),
                null,
                new Message(MINOR, null),
                new Message(MINOR, "Hello5")
        );
    }
}