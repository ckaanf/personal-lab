package ckaanf.cache.service.strategy.splitshardedsubbloomfilter;

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
public class ItemSplitShardedSubBloomFilterCacheService implements ItemCacheService {
    private final ItemService itemService;
    private final SplitShardedSubBloomFilterRedisHandler splitShardedSubBloomFilterRedisHandler;

    private static final SplitShardedSubBloomFilter bloomFilter = SplitShardedSubBloomFilter.create(
            "testId", 1000, 0.01, 4);

    @Override
    public ItemResponse read(Long itemId) {
        boolean result = splitShardedSubBloomFilterRedisHandler.mightContain(bloomFilter, String.valueOf(itemId));
        if (!result) {
            return null;
        }
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
        ItemResponse itemResponse = itemService.create(request);
        splitShardedSubBloomFilterRedisHandler.add(bloomFilter, String.valueOf(itemResponse.itemId()));
        return itemResponse;
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
        return CacheStrategy.SPLIT_SHARDED_SUB_BLOOM_FILTER == cacheStrategy;
    }
}
