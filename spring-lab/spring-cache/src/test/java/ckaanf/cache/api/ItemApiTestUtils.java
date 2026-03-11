package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import org.springframework.web.client.RestClient;

public class ItemApiTestUtils {
    static RestClient restClient = RestClient.create("http://localhost:8080");

    static ItemResponse read(CacheStrategy cacheStrategy, Long itemId) {
        return restClient.get()
                .uri("/cache-strategy/%s/items/%s".formatted(cacheStrategy.name(), itemId))
                .retrieve()
                .body(ItemResponse.class);
    }

    static ItemPageResponse readAll(CacheStrategy cacheStrategy, long page, long size) {
        return restClient.get()
                .uri("/cache-strategy/%s/items?page=%s&size=%s".formatted(cacheStrategy.name(), page, size))
                .retrieve()
                .body(ItemPageResponse.class);
    }

    static ItemPageResponse readAllInfiniteScroll(CacheStrategy cacheStrategy, Long lastItemId, long size) {
        return restClient.get()
                .uri(lastItemId == null ?
                        "/cache-strategy/%s/items/infinite-scroll?size=%s".formatted(cacheStrategy.name(), size) :
                        "/cache-strategy/%s/items/infinite-scroll?lastItemId=%s&size=%s".formatted(cacheStrategy.name(), lastItemId, size))
                .retrieve()
                .body(ItemPageResponse.class);
    }

    static ItemResponse create(CacheStrategy cacheStrategy, ItemCreateRequest request) {
        return restClient.post()
                .uri("/cache-strategy/%s/items".formatted(cacheStrategy.name()))
                .body(request)
                .retrieve()
                .body(ItemResponse.class);
    }

    static ItemResponse update(CacheStrategy cacheStrategy, Long itemId, ItemUpdateRequest request) {
        return restClient.put()
                .uri("/cache-strategy/%s/items/%s".formatted(cacheStrategy.name(), itemId))
                .body(request)
                .retrieve()
                .body(ItemResponse.class);
    }

    static void delete(CacheStrategy cacheStrategy, Long itemId) {
        restClient.delete()
                .uri("/cache-strategy/%s/items/%s".formatted(cacheStrategy.name(), itemId))
                .retrieve()
                .toBodilessEntity();
    }
}
