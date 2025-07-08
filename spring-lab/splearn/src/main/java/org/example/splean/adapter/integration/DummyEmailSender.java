package org.example.splean.adapter.integration;

import org.example.splean.application.required.EmailSender;
import org.example.splean.domain.Email;

public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        
    }
}
