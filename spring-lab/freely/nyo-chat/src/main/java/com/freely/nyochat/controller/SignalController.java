package com.freely.nyochat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SignalController {
    
    @MessageMapping("/signal")
    @SendTo("/topic/signal")
    public String signal(String message) {
        System.out.println("Received message: " + message);
        return message; //받은 메시지 브로드캐스트
    }
}
