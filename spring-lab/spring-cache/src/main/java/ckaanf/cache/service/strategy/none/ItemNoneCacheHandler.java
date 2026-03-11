package ckaanf.cache.service.strategy.none;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.common.cache.CustomCacheHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemNoneCacheHandler implements CustomCacheHandler {
    @Override
    public <T> T fetch(String key, Duration ttl, Supplier<T> dataSourceSupplier, Class<T> clazz) {
        log.info("[ItemNoneCacheHandler.fetch] key = {} ", key);
        return dataSourceSupplier.get();
    }

    @Override
    public void put(String key, Duration ttl, Object value) {
        log.info("[ItemNoneCacheHandler.put] key = {} ", key);
    }

    @Override
    public void evict(String key) {
        log.info("[ItemNoneCacheHandler.evict] key = {} ", key);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return cacheStrategy == CacheStrategy.NONE;
    }
}
