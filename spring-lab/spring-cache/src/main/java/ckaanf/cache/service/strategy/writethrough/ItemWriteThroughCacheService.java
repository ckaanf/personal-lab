package ckaanf.cache.service.strategy.writethrough;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.ItemCacheService;
import ckaanf.cache.service.ItemService;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemWriteThroughCacheService implements ItemCacheService {
    private final ItemService itemService;
    private final RedisRepository redisRepository;

    private static final String LIST_ID = "itemList";
    private static final Duration TIME_TO_LIVE = Duration.ofSeconds(5);


    @Override
    public ItemResponse read(Long itemId) {
        ItemResponse item = redisRepository.read(genRedisId(itemId), ItemResponse.class);
        if (item != null) {
            return item;
        }
        return itemService.read(itemId);
    }

    @Override
    public ItemPageResponse readAll(Long page, Long size) {
        List<ItemResponse> items = redisRepository.readAll(LIST_ID, page, size, ItemResponse.class);
        if (items.size() < size) {
            return itemService.readAll(page, size);
        }

        return ItemPageResponse.fromResponse(items, itemService.count());
    }

    @Override
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long size) {
        List<ItemResponse> items = redisRepository.readAllInfiniteScroll(LIST_ID, lastItemId, size, ItemResponse.class);
        if (items.size() < size) {
            return itemService.readAllInfiniteScroll(lastItemId, size);
        }

        return ItemPageResponse.fromResponse(items, itemService.count());
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        ItemResponse itemResponse = itemService.create(request);
        redisRepository.add(
                LIST_ID,
                genRedisId(itemResponse.itemId()),
                itemResponse,
                TIME_TO_LIVE,
                itemResponse.itemId()
        );
        return itemResponse;
    }

    @Override
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        ItemResponse itemResponse = itemService.update(itemId, request);
        redisRepository.add(
                LIST_ID,
                genRedisId(itemResponse.itemId()),
                itemResponse,
                TIME_TO_LIVE,
                itemResponse.itemId()
        );
        return itemResponse;
    }

    @Override
    public void delete(Long itemId) {
        itemService.delete(itemId);
        redisRepository.del(LIST_ID, genRedisId(itemId));
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.WRITE_THROUGH == cacheStrategy;
    }

    private String genRedisId(Long itemId) {
        return "item:%d".formatted(itemId);
    }
}
