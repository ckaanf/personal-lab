package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

public class ApplicationLevelShardingReplicationStrategyApiTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.APPLICATION_LEVEL_SHARDING_REPLICATION;

    @Test
    void test() {
        ItemResponse item = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data"));

        for (int i = 0; i < 3; i++) {
            ItemResponse read = ItemApiTestUtils.read(CACHE_STRATEGY, item.itemId());
            System.out.println("read = " + read);
        }

        ItemApiTestUtils.update(CACHE_STRATEGY, item.itemId(), new ItemUpdateRequest("updated"));
        ItemResponse updatedData = ItemApiTestUtils.read(CACHE_STRATEGY, item.itemId());
        System.out.println("updated = " + updatedData);

        ItemApiTestUtils.delete(CACHE_STRATEGY, item.itemId());
        ItemResponse deleted = ItemApiTestUtils.read(CACHE_STRATEGY, item.itemId());
        System.out.println("deleted = " + deleted);
    }

}
