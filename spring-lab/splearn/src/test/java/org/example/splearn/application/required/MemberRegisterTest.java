package org.example.splearn.application.required;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.example.splearn.SplearnTestConfiguration;
import org.example.splearn.application.provided.MemberRegister;
import org.example.splearn.domain.DuplicateEmailException;
import org.example.splearn.domain.Member;
import org.example.splearn.domain.MemberFixture;
import org.example.splearn.domain.MemberRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.splearn.domain.MemberStatus.ACTIVE;
import static org.example.splearn.domain.MemberStatus.PENDING;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    void register() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        System.out.println(member);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(PENDING);
    }

    @Test
    void activate() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void duplicateEmailFail() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest())).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void memberRegisterRequestFail() {
        checkValidation(new MemberRegisterRequest("ckaanf@splearn.app", "cka", "ckaanfckaanf"));
        checkValidation(new MemberRegisterRequest("ckaanf@splearn.app", "cka".repeat(10), "ckaanfckaanf"));

        checkValidation(new MemberRegisterRequest("ckaanfsplearn.app", "ckaanf", "ckaanfckaanf"));

        checkValidation(new MemberRegisterRequest("ckaanf@splearn.app", "ckaanf", "cka"));
        checkValidation(new MemberRegisterRequest("ckaanf@splearn.app", "ckaanf", "cka".repeat(100)));

    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
