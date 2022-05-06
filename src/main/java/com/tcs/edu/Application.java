package com.tcs.edu;

import com.tcs.edu.service.MessageService;

import static com.tcs.edu.project_enum.Doubling.*;
import static com.tcs.edu.project_enum.MessageOrder.*;
import static com.tcs.edu.project_enum.Severity.*;

class Application {
    public static void main(String[] args) {
        MessageService.process(REGULAR, DESC, DISTINCT,"Hello, world!",
                "test1", "test2", "test3", null, "test4", "test3", "test1", "test7");
    }
}