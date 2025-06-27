package org.example.splean.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    void createMember() {
        var member = new Member("ckaanf@splearn.app", "ckaanf", "secret");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void constructorNullCheck() {
        assertThatThrownBy(() -> new Member(null, "ckaanf", "secret"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void activate() {
        var member = new Member("ckaanf", "ckaanf", "secret");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
}