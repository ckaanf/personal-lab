package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

public class NonStrategyApiTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.NONE;

    @Test
    void createAndReadAndUpdateAndDelete() {
        ItemResponse created = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data"));
        System.out.println("created = " + created);

        ItemResponse read = ItemApiTestUtils.read(CACHE_STRATEGY, created.itemId());
        System.out.println("read1 = " + read);

        ItemResponse updated = ItemApiTestUtils.update(CACHE_STRATEGY, created.itemId(), new ItemUpdateRequest("updated data"));
        System.out.println("updated = " + updated);

        ItemResponse read2 = ItemApiTestUtils.read(CACHE_STRATEGY, created.itemId());
        System.out.println("read2 = " + read2);

        ItemApiTestUtils.delete(CACHE_STRATEGY, read.itemId());

        ItemResponse read3 = ItemApiTestUtils.read(CACHE_STRATEGY, created.itemId());
        System.out.println("read3 = " + read3);

    }

    @Test
    void readAll() {
        for (int i = 0; i < 3; i++) {
            ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
        }

        ItemPageResponse itemPage1 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1L, 2L);
        System.out.println("itemPage 1 = " + itemPage1);

        ItemPageResponse itemPage2 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 2L, 2L);
        System.out.println("itemPage 1 = " + itemPage2);
    }

    @Test
    void readAllInfiniteScroll() {
        for (int i = 0; i < 3; i++) {
            ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
        }

        ItemPageResponse itemPage1 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 2L);
        System.out.println("itemPage 1 = " + itemPage1);

        ItemPageResponse itemPage2 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY,
                itemPage1.items().getLast().itemId(), 2L);
        System.out.println("itemPage 1 = " + itemPage2);
    }

}
