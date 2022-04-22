package com.tcs.edu;

import com.tcs.edu.service.MessageService;

import static com.tcs.edu.decorator.Severity.REGULAR;

class Application {
    public static void main(String[] args) {
        MessageService.process(REGULAR,"Hello, world", null, "test");
    }
}