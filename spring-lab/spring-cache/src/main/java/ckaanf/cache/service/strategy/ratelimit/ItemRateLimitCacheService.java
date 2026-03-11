package ckaanf.cache.service.strategy.ratelimit;

import ckaanf.cache.common.cache.CacheStrategy;
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
public class ItemRateLimitCacheService implements ItemCacheService {
    private final ItemService itemService;
    private final RateLimiter rateLimiter;

    private static final String RATE_LIMITER_ID = "itemRead";
    private static final long RATE_LIMIT_COUNT = 10;
    private static final long RATE_LIMIT_PER_SECONDS = 1;

    @Override
    public ItemResponse read(Long itemId) {
        boolean allowed = rateLimiter.isAllowed(RATE_LIMITER_ID, RATE_LIMIT_COUNT, RATE_LIMIT_PER_SECONDS);
        if (allowed) {
            return itemService.read(itemId);
        }
        throw new RuntimeException("Rate limit exceeded");
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
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return itemService.update(itemId, request);
    }

    @Override
    public void delete(Long itemId) {
        itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return cacheStrategy == CacheStrategy.RATE_LIMIT;
    }
}
