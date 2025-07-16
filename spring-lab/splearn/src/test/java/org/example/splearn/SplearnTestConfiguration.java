package org.example.splearn;

import org.example.splearn.application.member.required.EmailSender;
import org.example.splearn.domain.MemberFixture;
import org.example.splearn.domain.member.PasswordEncoder;
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

