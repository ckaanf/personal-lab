package ckaanf.cache.service.strategy.per;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CacheDataTest {

    @Test
    void parseData() {
        CacheData cacheData = CacheData.of(1234L, 1000L, Duration.ofSeconds(10));
        System.out.println("cacheData = " + cacheData);

        assertThat(cacheData.getData()).isEqualTo("1234");
        assertThat(cacheData.parseData(Long.class)).isEqualTo(1234L);
    }

    @Test
    void shouldRecompute() throws InterruptedException {
        CacheData cacheData = CacheData.of(1234L, 1000L, Duration.ofSeconds(3));

        for (int i = 0; i < 30; i++) {
            boolean result = cacheData.shouldRecompute(1);
            System.out.println("result = " + result);
            TimeUnit.MILLISECONDS.sleep(100);
        }

    }

}