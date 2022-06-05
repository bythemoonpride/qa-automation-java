package com.tcs.edu;

import com.tcs.edu.domain.Message;
import com.tcs.edu.repository.HashMapMessageRepository;
import com.tcs.edu.service.MessageService;
import com.tcs.edu.service.OrderedDistinctedMessageService;

import java.util.UUID;

import static com.tcs.edu.project_enum.Severity.*;

class Application {
    public static void main(String[] args) {

        MessageService messageService = new OrderedDistinctedMessageService(new HashMapMessageRepository());

//        Попытки создать Message с null вместо параметров
//        UUID messageId000 = messageService.create(new Message(null, "Hello"));
//        UUID messageId001 = messageService.create(new Message(REGULAR, null));
//        UUID messageId002 = messageService.create(new Message(null, null));

        //Создание
        UUID messageId0 = messageService.create(new Message(REGULAR, "Hello"));
        UUID messageId1 = messageService.create(new Message(REGULAR, "Hello!!!"));
        UUID messageId2 = messageService.create(new Message(MINOR, "Hello"));
        UUID messageId3 = messageService.create(new Message(MINOR, "Hello!!!"));
        UUID messageId4 = messageService.create(new Message(MINOR, "I'll be deleted one day"));
        UUID messageId5 = messageService.create(new Message(REGULAR, "I'll be updated one day"));

        //Получение
        System.out.println("Добавлены следующие сообщения:");
        System.out.println(messageService.findById(messageId0));
        System.out.println(messageService.findById(messageId1));
        System.out.println(messageService.findById(messageId2));
        System.out.println(messageService.findById(messageId3));
        System.out.println(messageService.findById(messageId4));
        System.out.println(messageService.findById(messageId5));
        System.out.println("\n===========================================================\n");

        //Обновление
        System.out.println("Обновлены следующие сообщения:");
        Message messageForUpdate = new Message(MAJOR, "Now i am updated!!!");
        messageForUpdate.setId(messageId5);
        System.out.println(messageService.put(messageForUpdate));
        System.out.println(messageService.findById(messageId5));

        System.out.println("Попытка обновления сообщения с id, которого нет в HashMapMessageRepository:");
        Message messageForUpdateRandomId = new Message(MAJOR, "Now i am updated!!!");
        messageForUpdateRandomId.setId(UUID.randomUUID());
        System.out.println(messageService.put(messageForUpdateRandomId));

        System.out.println("Попытка обновления сообщения с id = null:");
        System.out.println(messageService.put(new Message(MAJOR, "Some body with NULL id")));
        System.out.println("\n===========================================================\n");

        //Удаление
        System.out.println("Удаление сообщения:");
        System.out.println(messageService.delete(messageId4));
        System.out.println(messageService.findById(messageId4));

        System.out.println("Попытка удаления сообщения, id которого нет в HashMapMessageRepository:");
        System.out.println(messageService.delete(UUID.randomUUID()));

        System.out.println("Попытка удаления сообщения c id = null");
        System.out.println(messageService.delete(null));
        System.out.println("\n===========================================================\n");

        System.out.println("Фильтрация сообщений:");
        System.out.println(messageService.get());
        System.out.println(messageService.get(MINOR));
        System.out.println(messageService.get("Hello"));
        System.out.println(messageService.get(REGULAR, "Hello"));
//        Попытка получения сообщений с null в параметрах фильтрации
//        System.out.println(messageService.get(null, "Hello"));
//        System.out.println(messageService.get(REGULAR, null));
//        System.out.println(messageService.get(null, null));


    }

}