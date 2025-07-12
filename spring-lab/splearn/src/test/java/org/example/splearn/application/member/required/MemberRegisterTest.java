package org.example.splearn.application.member.required;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.example.splearn.SplearnTestConfiguration;
import org.example.splearn.application.member.provided.MemberRegister;
import org.example.splearn.domain.MemberFixture;
import org.example.splearn.domain.member.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.splearn.domain.member.MemberStatus.DEACTIVATED;
import static org.example.splearn.domain.member.MemberStatus.PENDING;

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
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();
    }

    @Test
    void deactivate() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(DEACTIVATED);
        assertThat(member.getDetail().getDeActivatedAt()).isNotNull();
    }

    @Test
    void updateInfo() {
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("ckaanf", "ckaanf", "ckaanf"));

        assertThat(member.getNickname()).isEqualTo("ckaanf");
        assertThat(member.getDetail().getProfile().address()).isEqualTo("ckaanf");
        assertThat(member.getDetail().getIntroduction()).isEqualTo("ckaanf");
    }

    @Test
    void updateInfoFail() {
        Member member = registerMember();
        memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("ckaanf", "ckaanf", "ckaanf"));

        Member member2 = registerMember("ckaanfckaanf@test.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        // member2는 기존의 member와 같은 프로필 주소를 사용할 수 없다
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("참참234", "ckaanf", "참참"));
        }).isInstanceOf(DuplicateProfileException.class);

        // 다른 프로필 주소로는 변경 가능
        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("참참234", "ckaanf2", "참참"));

        // 기존 프로필 주소를 바꾸는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("ckaanf", "ckaanf", "참참"));

        // 프로필 주소를 제거하는 것도 가능
        memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("ckaanf", "", "참참"));


        // 프로필 주소 중복은 허용하지 않음
        assertThatThrownBy(() -> {
            memberRegister.updateInfo(member.getId(), new MemberInfoUpdateRequest("ckaanf", "ckaanf2", "참참"));
        }).isInstanceOf(DuplicateProfileException.class);


    }

    @Test
    void duplicateEmailFail() {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());
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

    private Member registerMember() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
