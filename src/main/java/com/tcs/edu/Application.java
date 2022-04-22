package com.tcs.edu;

import com.tcs.edu.service.MessageService;

import static com.tcs.edu.project_enum.MessageOrder.*;
import static com.tcs.edu.project_enum.Severity.*;

class Application {
    public static void main(String[] args) {
        MessageService.process(REGULAR, DESC, "Hello, world!",
                "test1", "test2", "test3", null, "test4", "test5", "test6", "test7");
    }
}