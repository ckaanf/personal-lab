package org.example.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    void equality() {
        var email1 = new Email("test@gmail.com");
        var email2 = new Email("test@gmail.com");

        assertThat(email1.equals(email2));
    }

}