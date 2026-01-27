package ckaanf.cache.service.strategy.none;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemNoneCacheService implements ItemCacheService {
    private final ItemService itemService;

    @Override
    @CustomCacheable(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "item",
            key = "#itemId",
            ttlSeconds = 5
    )
    public ItemResponse read(Long itemId) {
        return itemService.read(itemId);
    }

    @Override
    @CustomCacheable(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "itemList",
            key = "#page + ':' + #size",
            ttlSeconds = 5
    )
    public ItemPageResponse readAll(Long page, Long size) {
        return itemService.readAll(page, size);
    }

    @Override
    @CustomCacheable(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "itemListInfiniteScroll",
            key = "#lastItemId + ':' + #size",
            ttlSeconds = 5
    )
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long size) {
        return itemService.readAllInfiniteScroll(lastItemId, size);
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        return itemService.create(request);
    }

    @Override
    @CustomCachePut(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "item",
            key = "#itemId",
            ttlSeconds = 5
    )
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return itemService.update(itemId, request);
    }

    @Override
    @CustomCacheEvict(
            cacheStrategy = CacheStrategy.NONE,
            cacheName = "item",
            key = "#itemId"
    )
    public void delete(Long itemId) {
        itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return cacheStrategy == CacheStrategy.NONE;
    }
}
