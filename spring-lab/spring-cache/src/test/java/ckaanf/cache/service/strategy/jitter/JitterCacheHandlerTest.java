package ckaanf.cache.service.strategy.jitter;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class JitterCacheHandlerTest extends RedisTestContainerSupport {
    @Autowired
    JitterCacheHandler jitterCacheHandler;


    @Test
    void put() {
        // given, when
        jitterCacheHandler.put(
                "testKey",
                Duration.ofSeconds(10),
                String.class
        );

        //then
        Long ttlSeconds = redisTemplate.getExpire("testKey", TimeUnit.SECONDS);
        System.out.println("ttlSeconds = " + ttlSeconds);
        assertThat(ttlSeconds).isGreaterThanOrEqualTo(7);
        assertThat(ttlSeconds).isLessThanOrEqualTo(13);
    }

    @Test
    void put_shouldThrowException_whenTtlLessThanOrEqualToJitterRangeSeconds() {
        assertThatThrownBy(() ->
                jitterCacheHandler.put("testKey", Duration.ofSeconds(3), String.class)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void evict() {
        // given, when
        jitterCacheHandler.put("testKey", Duration.ofSeconds(10), String.class);

        jitterCacheHandler.evict("testKey");

        // then
        String result = redisTemplate.opsForValue().get("testKey");
        assertThat(result).isNull();
    }

    @Test
    void fetch() {
        String result1 = fetchData();
        String result2 = fetchData();
        String result3 = fetchData();

        assertThat(result1).isEqualTo("sourceData");
        assertThat(result2).isEqualTo("sourceData");
        assertThat(result3).isEqualTo("sourceData");
    }

    private String fetchData() {
        return jitterCacheHandler.fetch(
                "testKey",
                Duration.ofSeconds(10),
                () -> {
                    System.out.println("fetch source data");
                    return "sourceData";
                },
                String.class
        );
    }
}