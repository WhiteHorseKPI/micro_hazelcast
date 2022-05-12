package com.microservices.messagesservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MessagesController {

    final String stubMessage = "messages-service is not implemented yet";

    @GetMapping("/message")
    public String getStub() {
        return stubMessage;
    }
}
