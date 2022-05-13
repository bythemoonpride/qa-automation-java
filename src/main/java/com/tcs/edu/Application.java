package com.tcs.edu;

import com.tcs.edu.decorator.TimestampPaginationDecorator;
import com.tcs.edu.domain.Message;
import com.tcs.edu.printer.ConsolePrinter;
import com.tcs.edu.service.MessageService;
import com.tcs.edu.service.OrderedDistinctedMessageService;

import java.sql.SQLOutput;

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
                new Message(MAJOR, "Hello1"),
                new Message(REGULAR, "Hello2"),
                null,
                new Message(MINOR, null),
                new Message(MINOR, "Hello3")
        );

        System.out.println("\n\n");
        System.out.println("Вывод сообщения, переданного на печать как объект:");
        System.out.println(new Message(MINOR, "Hello!"));


        Message message1 = new Message(MAJOR, "Hi");
        Message message2 = new Message(MAJOR, "Hi");
        Message message3 = new Message(MAJOR, "Hello there");

        System.out.println("\n");
        System.out.println("Сравнение сообщений, сообщения одинаковые?: " + message1.equals(message2));
        System.out.println("Сравнение сообщений, сообщения одинаковые?: " + message1.equals(message3));

        System.out.println("\n");
        System.out.println("Сравнение хэшкодов одинаковых сообщений, хэшкоды совпадают?:");
        System.out.println(message1.hashCode() == message2.hashCode());

        System.out.println("Сравнение хэшкодов неодинаковых сообщений, хэшкоды совпадают?:");
        System.out.println(message1.hashCode() == message3.hashCode());

        System.out.println("\n");
        System.out.println("Хэшкод сообщения: " + message1.hashCode());
    }
}