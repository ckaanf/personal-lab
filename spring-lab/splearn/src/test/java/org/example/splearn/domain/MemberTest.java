package org.example.splearn.domain;

import org.example.splearn.domain.member.Member;
import org.example.splearn.domain.member.MemberInfoUpdateRequest;
import org.example.splearn.domain.member.MemberStatus;
import org.example.splearn.domain.member.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.splearn.domain.MemberFixture.createMemberRegisterRequest;
import static org.example.splearn.domain.MemberFixture.createPasswordEncoder;

class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = createPasswordEncoder();
        member = Member.register(createMemberRegisterRequest("ckaanf@splearn.app"), passwordEncoder);
    }

    @Test
    void registerMember() {

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
        assertThat(member.getDetail().getRegisteredAt()).isNotNull();
    }

    @Test
    void activate() {
        assertThat(member.getDetail().getActivatedAt()).isNull();

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void activateFail() {

        member.activate();

        assertThatThrownBy(member::activate).isInstanceOf(IllegalStateException.class);

    }

    @Test
    void deactivate() {

        member.activate();
        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeActivatedAt()).isNotNull();
    }

    @Test
    void deactivateFail() {

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword() {
        assertThat(member.verifyPassword("secretsecret", passwordEncoder)).isTrue();
        assertThat(member.verifyPassword("hellohellohello", passwordEncoder)).isFalse();
    }

    @Test
    void changePassword() {
        member.changePassword("newSecret", passwordEncoder);

        assertThat(member.verifyPassword("newSecret", passwordEncoder)).isTrue();
    }

    @Test
    void isActive() {
        assertThat(member.isActive()).isFalse();

        member.activate();
        assertThat(member.isActive()).isTrue();

        member.deactivate();
        assertThat(member.isActive()).isFalse();
    }

    @Test
    void inValidEmail() {
        assertThatThrownBy(() ->
                Member.register(createMemberRegisterRequest("test"), passwordEncoder)
        ).isInstanceOf(IllegalArgumentException.class);

        Member.register(createMemberRegisterRequest("test_test@gmail.com"), passwordEncoder);

    }

    @Test
    void updateInfo() {
        member.activate();
        var request = new MemberInfoUpdateRequest(
                "참참",
                "ckacka",
                "참참"
        );

        member.updateInfo(request);

        assertThat(member.getNickname()).isEqualTo("참참");
        assertThat(member.getDetail().getIntroduction()).isEqualTo("참참");
        assertThat(member.getDetail().getProfile().address()).isEqualTo("ckacka");
    }

    @Test
    void updateInfoFail() {

        assertThatThrownBy(() -> {
                    var request = new MemberInfoUpdateRequest(
                            "참참",
                            "ckacka",
                            "참참"
                    );
                    member.updateInfo(request);
                }
        ).isInstanceOf(IllegalStateException.class);

    }

}