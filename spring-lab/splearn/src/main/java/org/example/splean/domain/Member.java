package org.example.splean.domain;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Member {
    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    public Member(String email, String nickname, String passwordHash) {
        this.email = Objects.requireNonNull(email);
        this.nickname = nickname;
        this.passwordHash = passwordHash;

        this.status = MemberStatus.PENDING;
    }

    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }
}
