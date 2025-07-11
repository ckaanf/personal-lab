package org.example.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    @Test
    void profile() {
        new Profile("ckaanf");
        new Profile("ckaanf100");
        new Profile("12345");
        new Profile("");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("ckaanf10012131415")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        var profile = new Profile("ckaanf");

        assertThat(profile.url()).isEqualTo("@ckaanf");
    }

}