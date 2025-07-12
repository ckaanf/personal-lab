package org.example.splearn.adapter.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurePasswordEncoderTest {
    @Test
    void securePasswordEncoder() {
        SecurePasswordEncoder securePasswordEncoder = new SecurePasswordEncoder();

        String passwordHash = securePasswordEncoder.encode("<PASSWORD>");

        assertTrue(securePasswordEncoder.matches("<PASSWORD>", passwordHash));
        assertFalse(securePasswordEncoder.matches("<PASSWORD2>", passwordHash));
    }

}