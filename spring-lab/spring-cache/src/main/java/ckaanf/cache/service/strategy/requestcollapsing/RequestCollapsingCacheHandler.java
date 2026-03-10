package ckaanf.cache.service.strategy.requestcollapsing;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.common.cache.CustomCacheHandler;
import ckaanf.cache.common.distributedlock.DistributedLockProvider;
import ckaanf.cache.common.serde.DataSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RequestCollapsingCacheHandler implements CustomCacheHandler {
    private final StringRedisTemplate redisTemplate;
    private final DistributedLockProvider distributedLockProvider;

    private static final long POLLING_INTERVAL_MILLIS = 50;
    private static final long REFRESH_WAITING_TIMEOUT_MILLIS = 2000;

    @Override
    public <T> T fetch(String key, Duration ttl, Supplier<T> dataSourceSupplier, Class<T> clazz) {
        String cache = redisTemplate.opsForValue().get(key);
        if (cache != null) {
            return DataSerializer.deserializeOrNull(cache, clazz);
        }

        String lockKey = genLockKey(key);
        if (distributedLockProvider.lock(lockKey, Duration.ofSeconds(3))) {
            try {
                return refresh(key, ttl, dataSourceSupplier);
            } finally {
                distributedLockProvider.unlock(lockKey);

            }
        }

        long start = System.nanoTime();
        while (System.nanoTime() - start < TimeUnit.MILLISECONDS.toNanos(REFRESH_WAITING_TIMEOUT_MILLIS)) {
            cache = redisTemplate.opsForValue().get(key);
            if (cache != null) {
                return DataSerializer.deserializeOrNull(cache, clazz);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(POLLING_INTERVAL_MILLIS);
            } catch (InterruptedException e) {
                break;
            }

        }

        return refresh(key, ttl, dataSourceSupplier);
    }

    private String genLockKey(String key) {
        return CacheStrategy.REQUEST_COLLAPSING + ":lock:" + key;
    }

    private <T> T refresh(String key, Duration ttl, Supplier<T> dataSourceSupplier) {
        T sourceResult = dataSourceSupplier.get();
        put(key, ttl, sourceResult);
        return sourceResult;
    }

    @Override
    public void put(String key, Duration ttl, Object value) {
        redisTemplate.opsForValue().set(key, DataSerializer.serializeOrException(value), ttl);
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.REQUEST_COLLAPSING == cacheStrategy;
    }
}
