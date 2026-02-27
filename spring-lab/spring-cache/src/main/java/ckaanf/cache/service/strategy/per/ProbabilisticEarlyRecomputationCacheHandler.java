package ckaanf.cache.service.strategy.per;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.common.cache.CustomCacheHandler;
import ckaanf.cache.common.serde.DataSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class ProbabilisticEarlyRecomputationCacheHandler implements CustomCacheHandler {
    private final StringRedisTemplate redisTemplate;

    @Override
    public <T> T fetch(String key, Duration ttl, Supplier<T> dataSourceSupplier, Class<T> clazz) {
        String cached = redisTemplate.opsForValue().get(key);

        if (cached == null) {
            return refresh(key, ttl, dataSourceSupplier);
        }

        CacheData cacheData = DataSerializer.deserializeOrNull(cached, CacheData.class);
        if (cached == null) {
            return refresh(key, ttl, dataSourceSupplier);
        }

        if (cacheData.shouldRecompute(1)) {
            return refresh(key, ttl, dataSourceSupplier);
        }

        T data = cacheData.parseData(clazz);
        if (data == null) {
            return refresh(key, ttl, dataSourceSupplier);
        }

        return data;
    }

    private <T> T refresh(String key, Duration ttl, Supplier<T> dataSourceSupplier) {
        long startMillis = Instant.now().toEpochMilli();
        T sourceResult = dataSourceSupplier.get();
        long computationTimeMillis = Instant.now().toEpochMilli() - startMillis;
        put(key, ttl, sourceResult, computationTimeMillis);
        return sourceResult;
    }

    private <T> void put(String key, Duration ttl, Object data, long computationTimeMillis) {
        CacheData cacheData = CacheData.of(data, computationTimeMillis, ttl);
        redisTemplate.opsForValue().set(key, DataSerializer.serializeOrException(cacheData), ttl);
    }

    @Override
    public void put(String key, Duration ttl, Object value) {
        put(key, ttl, value, 100);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.PROBABILISTIC_EARLY_RECOMPUTATION == cacheStrategy;
    }
}
