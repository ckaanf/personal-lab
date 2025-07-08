package org.example.splean.application.required;

import jakarta.validation.ConstraintViolationException;
import org.example.splean.SplearnTestConfiguration;
import org.example.splean.application.provided.MemberRegister;
import org.example.splean.domain.DuplicateEmailException;
import org.example.splean.domain.Member;
import org.example.splean.domain.MemberFixture;
import org.example.splean.domain.MemberRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.splean.domain.MemberStatus.PENDING;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(MemberRegister memberRegister) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(PENDING);
    }

    @Test
    void duplicateEmailFail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest())).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        extracted(new MemberRegisterRequest("ckaanf@splearn.app", "cka", "ckaanfckaanf"));
        extracted(new MemberRegisterRequest("ckaanf@splearn.app", "cka".repeat(10), "ckaanfckaanf"));

        extracted(new MemberRegisterRequest("ckaanfsplearn.app", "ckaanf", "ckaanfckaanf"));

        extracted(new MemberRegisterRequest("ckaanf@splearn.app", "ckaanf", "cka"));
        extracted(new MemberRegisterRequest("ckaanf@splearn.app", "ckaanf", "cka".repeat(100)));

    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
