package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

public class WriteThroughStrategyApiTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.WRITE_THROUGH;


    @Test
    void test() {
        for (int i = 0; i < 120; i++) {
            ItemResponse item = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
            ItemResponse response = ItemApiTestUtils.read(CACHE_STRATEGY, item.itemId());
            System.out.println("response = " + response);
        }

        ItemPageResponse readAllPage1 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1, 60);
        ItemPageResponse readAllPage2 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 2, 60);
        System.out.println("readAllPage1.items().size() = " + readAllPage1.items().size());
        System.out.println("readAllPage2.items().size() = " + readAllPage2.items().size());

        ItemPageResponse readAllInfiniteScrollPage1 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 60);
        ItemPageResponse readAllInfiniteScrollPage2 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, readAllInfiniteScrollPage1.items().getLast().itemId(), 60);


        System.out.println("readAllInfiniteScrollPage2.items().size() = " + readAllInfiniteScrollPage1.items().size());
        System.out.println("readAllInfiniteScrollPage2.items().size() = " + readAllInfiniteScrollPage2.items().size());

        ItemResponse item = readAllPage1.items().getFirst();
        ItemResponse updated = ItemApiTestUtils.update(CACHE_STRATEGY, item.itemId(), new ItemUpdateRequest("updated"));
        System.out.println("updated = " + updated);

        ItemApiTestUtils.delete(CACHE_STRATEGY, item.itemId());

        ItemResponse deleted = ItemApiTestUtils.read(CACHE_STRATEGY, item.itemId());
        System.out.println("deleted = " + deleted);

        readAllPage1 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1, 60);
        System.out.println("readAllPage1.items().getFirst() = " + readAllPage1.items().getFirst());

        readAllInfiniteScrollPage1 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 60);
        System.out.println("readAllInfiniteScrollPage1.items().getFirst() = " + readAllInfiniteScrollPage1.items().getFirst());



    }
}
