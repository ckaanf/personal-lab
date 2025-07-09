package org.example.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class SpleanApplicationTest {

    @Test
    void run() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            SpleanApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(SpleanApplication.class, new String[]{}));
        }
    }

}