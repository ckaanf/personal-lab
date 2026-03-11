package ckaanf.cache.service.strategy.applicationlevelshardingreplication;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.common.cache.CustomCacheHandler;
import ckaanf.cache.common.serde.DataSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationLevelShardingApplicationCacheHandler implements CustomCacheHandler {
    private final StringRedisTemplate redisTemplate;
    private final ShardedKeyGenerator shardedKeyGenerator;

    private static final int SHARD_REPLICATION_COUNT = 3;

    @Override
    public <T> T fetch(String key, Duration ttl, Supplier<T> dataSourceSupplier, Class<T> clazz) {
        String shardedKey = shardedKeyGenerator.findRandomShardedKey(key, SHARD_REPLICATION_COUNT);
        String cached = redisTemplate.opsForValue().get(shardedKey);

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
        String serializedValue = DataSerializer.serializeOrException(value);
        List<String> shardedKeys = shardedKeyGenerator.genShardedKeys(key, SHARD_REPLICATION_COUNT);
        for (String shardedKey : shardedKeys) {
            log.info("[ApplicationLevelShardingApplicationCacheHandler.put] shardedKey = {}");
            redisTemplate.opsForValue().set(shardedKey, serializedValue, ttl);
        }
    }

    @Override
    public void evict(String key) {
        List<String> shardedKeys = shardedKeyGenerator.genShardedKeys(key, SHARD_REPLICATION_COUNT);
        for (String shardedKey : shardedKeys) {
            log.info("[ApplicationLevelShardingApplicationCacheHandler.evict] shardedKey = {}");
            redisTemplate.delete(shardedKey);
        }
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.APPLICATION_LEVEL_SHARDING_REPLICATION == cacheStrategy;
    }
}
