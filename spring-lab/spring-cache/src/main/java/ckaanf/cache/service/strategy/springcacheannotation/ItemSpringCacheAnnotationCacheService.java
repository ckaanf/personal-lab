package ckaanf.cache.service.strategy.springcacheannotation;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.ItemCacheService;
import ckaanf.cache.service.ItemService;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemSpringCacheAnnotationCacheService implements ItemCacheService {
    private final ItemService itemService;

    @Override
    @Cacheable(cacheNames = "item", key = "#itemId")
    public ItemResponse read(Long itemId) {
        return itemService.read(itemId);
    }

    @Override
    @Cacheable(cacheNames = "itemList", key = "#page + ':' + #size")
    public ItemPageResponse readAll(Long page, Long size) {
        return itemService.readAll(page, size);
    }

    @Override
    @Cacheable(cacheNames = "itemListInfiniteScroll", key = "#lastItemId + ':' + #size")
    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long size) {
        return itemService.readAllInfiniteScroll(lastItemId, size);
    }

    @Override
    public ItemResponse create(ItemCreateRequest request) {
        return itemService.create(request);
    }

    @Override
    @CachePut(cacheNames = "item", key = "#itemId")
    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        return itemService.update(itemId, request);
    }

    @Override
    @CacheEvict(cacheNames = "item", key = "#itemId")
    public void delete(Long itemId) {
        itemService.delete(itemId);
    }

    @Override
    public boolean supports(CacheStrategy cacheStrategy) {
        return CacheStrategy.SPRING_CACHE_ANNOTATION == cacheStrategy;
    }
}
