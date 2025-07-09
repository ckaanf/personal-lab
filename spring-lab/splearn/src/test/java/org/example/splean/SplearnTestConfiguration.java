package org.example.splean;

import org.example.splean.application.required.EmailSender;
import org.example.splean.domain.MemberFixture;
import org.example.splean.domain.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SplearnTestConfiguration {
    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> {
            System.out.println("Email sent to: " + email);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.createPasswordEncoder();
    }
}

