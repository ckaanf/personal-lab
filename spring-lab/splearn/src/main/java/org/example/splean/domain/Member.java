package org.example.splean.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import static java.util.Objects.requireNonNull;

@Getter
@ToString
public class Member {
    private Email email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member() {

    }

    public static Member create(MemberCreateRequest memberCreateRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(memberCreateRequest.email());
        member.nickname = requireNonNull(memberCreateRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(memberCreateRequest.password()));

        member.status = MemberStatus.PENDING;
        return member;
    }

    public void activate() {
        Assert.state(status == MemberStatus.PENDING, "Member is not in PENDING status");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "Member is not in ACTIVE status");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.passwordHash = requireNonNull(passwordEncoder).encode(password);
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }
}
