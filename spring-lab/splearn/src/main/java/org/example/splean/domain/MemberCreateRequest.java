package org.example.splean.domain;

public record MemberCreateRequest(String email, String nickname, String password) {
}