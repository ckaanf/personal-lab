package org.example.splean.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.springframework.util.Assert;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NaturalIdCache
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public static Member register(MemberRegisterRequest memberRegisterRequest, PasswordEncoder passwordEncoder) {
        Member member = new Member();

        member.email = new Email(memberRegisterRequest.email());
        member.nickname = requireNonNull(memberRegisterRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(memberRegisterRequest.password()));

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
