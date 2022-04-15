package com.tcs.edu;

import com.tcs.edu.decorator.Severity;
import com.tcs.edu.service.MessageService;

class Application {
    public static void main(String[] args) {
        MessageService.process("Hello, world!", Severity.MINOR);
        MessageService.process("Hello, world!", Severity.REGULAR);
        MessageService.process("Hello, world!", Severity.MAJOR);
        MessageService.process("Hello, world!", Severity.MAJOR);
        MessageService.process("Hello, world!", Severity.REGULAR);
        MessageService.process("Hello, world!", Severity.MINOR);
    }
}