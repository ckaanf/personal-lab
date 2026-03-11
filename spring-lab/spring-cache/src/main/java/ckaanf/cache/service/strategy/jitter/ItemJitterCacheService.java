package ckaanf.cache.service.strategy.jitter;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.common.cache.CustomCacheEvict;
import ckaanf.cache.common.cache.CustomCachePut;
import ckaanf.cache.common.cache.CustomCacheable;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.ItemCacheService;
import ckaanf.cache.service.ItemService;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemJitterCacheService implements ItemCacheService {
    private final ItemService itemService;

    @Override
    @CustomCacheable(
            cacheStrategy = CacheStrategy.JITTER,
            cacheName = "item",
            key = "#itemId",
            ttlSeconds = 5)
    public ItemResponse read(Long itemId) {
        return itemService.read(itemId);
    }

    @Override
    public ItemPageResponse readAll(Long page, Long size) {
        return itemService.readAll(page, size);
    }

    @Override
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long size) {
        return itemService.readAllInfiniteScroll(lastItemId, size);
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        return itemService.create(request);
    }

    @Override
    @CustomCachePut(
            cacheStrategy = CacheStrategy.JITTER,
            cacheName = "item",
            key = "#itemId",
            ttlSeconds = 5)
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return itemService.update(itemId, request);
    }

    @Override
    @CustomCacheEvict(
            cacheStrategy = CacheStrategy.JITTER,
            cacheName = "item",
            key = "#itemId")
    public void delete(Long itemId) {
        itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return cacheStrategy == CacheStrategy.JITTER;
    }
}
