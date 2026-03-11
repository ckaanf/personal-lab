package ckaanf.cache.common.distributedlock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class DistributedLockProvider {
    private final StringRedisTemplate redisTemplate;

    public boolean lock(String id, Duration ttl) {
        Boolean result = redisTemplate.opsForValue().setIfAbsent(genKey(id), "", ttl);
        return result != null && result;
    }

    public void unlock(String id) {
        redisTemplate.delete(genKey(id));
    }

    private String genKey(String id) {
        return "distributed-lock:%s".formatted(id);
    }
}
