package ckaanf.cache.service.strategy.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RateLimiter {
    private final StringRedisTemplate redisTemplate;

    public boolean isAllowed(String id, long limit, long perSeconds) {
        String key = genKey(id);
        Long countResult = redisTemplate.opsForValue().increment(key);
        if (countResult == null) {
            return false;
        }

        long count = countResult;
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(perSeconds));
        }

        if (count <= limit) {
            return true;
        }

        if (count % (limit / 10) == 0 && redisTemplate.getExpire(key) == -1) {
            redisTemplate.expire(key, Duration.ofSeconds(perSeconds));
        }

        return false;
    }

    private String genKey(String id) {
        return "rate-limit:%s".formatted(id);
    }
}
