package ckaanf.cache.service.strategy.nullobjectpattern;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.ItemCacheService;
import ckaanf.cache.service.ItemService;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemNullObjectPatternCacheService implements ItemCacheService {
    private final ItemService itemService;

    /**
     * 더욱 유연한 구조의 nullObject도 가능
     * 예시) 데이터가 정말 없는가, 비공개 처리인가 등등
     */
    private static final ItemResponse nullObject = new ItemResponse(null, null);

    @Override
    @Cacheable(cacheNames = "item", key = "#itemId")
    public ItemResponse read(Long itemId) {
        ItemResponse itemResponse = itemService.read(itemId);
        if(itemResponse ==  null) {
            return nullObject;
        }
        return itemResponse;
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

    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.NULL_OBJECT_PATTERN == cacheStrategy;
    }
}
