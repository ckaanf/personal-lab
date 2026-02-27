package ckaanf.cache.service.strategy.per;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProbabilisticEarlyRecomputationCacheHandlerTest extends RedisTestContainerSupport {

    @Autowired
    ProbabilisticEarlyRecomputationCacheHandler handler;

    @Test
    void put() {
        handler.put("testKey", Duration.ofSeconds(10), "testValue");

        String result = redisTemplate.opsForValue().get("testKey");
        assertThat(result).isNotNull();
        System.out.println("result = " + result);
    }

    @Test
    void evict() {
        handler.put("testKey", Duration.ofSeconds(10), "testValue");

        handler.evict("testKey");

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
        return handler.fetch(
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