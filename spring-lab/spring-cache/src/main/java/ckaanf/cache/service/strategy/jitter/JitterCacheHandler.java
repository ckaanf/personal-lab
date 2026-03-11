package ckaanf.cache.service.strategy.jitter;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.common.cache.CustomCacheHandler;
import ckaanf.cache.common.serde.DataSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

@Component
@RequiredArgsConstructor
public class JitterCacheHandler implements CustomCacheHandler {
    private final StringRedisTemplate redisTemplate;
    public static final int JITTER_RANGE_SECONDS = 3;

    @Override
    public <T> T fetch(String key, Duration ttl, Supplier<T> dataSourceSupplier, Class<T> clazz) {
        String cached = redisTemplate.opsForValue().get(key);
        if (cached == null) {
            return refresh(key, ttl, dataSourceSupplier);
        }

        T data = DataSerializer.deserializeOrNull(cached, clazz);
        if (data == null) {
            return refresh(key, ttl, dataSourceSupplier);
        }

        return data;
    }

    private <T> T refresh(String key, Duration ttl, Supplier<T> dataSourceSupplier) {
        T sourceResult = dataSourceSupplier.get();
        put(key, ttl, sourceResult);
        return sourceResult;
    }

    @Override
    public void put(String key, Duration ttl, Object value) {
        redisTemplate.opsForValue().set(key, DataSerializer.serializeOrException(value), applyJitter(ttl));
    }

    private Duration applyJitter(Duration ttl) {
        if (ttl.getSeconds() <= JITTER_RANGE_SECONDS) {
            throw new IllegalArgumentException("ttl must be greater than " + JITTER_RANGE_SECONDS);
        }
        int jitter = RandomGenerator.getDefault().nextInt(-JITTER_RANGE_SECONDS, JITTER_RANGE_SECONDS + 1);
        return ttl.plusSeconds(jitter);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return cacheStrategy == CacheStrategy.JITTER;
    }
}
