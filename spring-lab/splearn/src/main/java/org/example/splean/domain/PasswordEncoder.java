package org.example.splean.domain;

public interface PasswordEncoder {
    String encode(String password);

    boolean matches(String password, String passwordHash);
}
