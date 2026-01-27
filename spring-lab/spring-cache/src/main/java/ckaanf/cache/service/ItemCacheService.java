package ckaanf.cache.service;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;

public interface ItemCacheService {
    ItemResponse read(Long itemId);

    ItemPageResponse readAll(Long page, Long size);

    ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long size);

    ItemResponse create(ItemCreateRequest request);

    ItemResponse update(Long itemId, ItemUpdateRequest request);

    void delete(Long itemId);

    boolean supports(CacheStrategy cacheStrategy);
}
