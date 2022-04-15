package com.tcs.edu;

import com.tcs.edu.service.MessageService;
import static com.tcs.edu.decorator.Severity.REGULAR;

class Application {
    public static void main(String[] args) {
        MessageService.process(REGULAR, "Hello, world!0", "Hello, world!1", "Hello, world!2",
                "Hello, world!3", "Hello, world!4", "Hello, world!5");
    }
}