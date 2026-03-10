package ckaanf.cache.service.strategy.requestcollapsing;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RequestCollapsingCacheHandlerTest extends RedisTestContainerSupport {

    @Autowired
    RequestCollapsingCacheHandler handler;

    @Test
    void put() {
        handler.put("testKey", Duration.ofSeconds(10), "data");

        String result = redisTemplate.opsForValue().get("testKey");
        assertThat(result).isNotNull();
        System.out.println("result = " + result);
    }

}