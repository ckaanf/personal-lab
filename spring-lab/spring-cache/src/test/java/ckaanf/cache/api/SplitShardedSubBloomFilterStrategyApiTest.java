package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import org.junit.jupiter.api.Test;

public class SplitShardedSubBloomFilterStrategyApiTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.SPLIT_SHARDED_SUB_BLOOM_FILTER;

    @Test
    void test() {
        for (int i = 0; i < 1000 + 2000 + 4000; i++) {
            ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data" + i));
        }

        for (long itemId = 10000; itemId < 11000; itemId++) {
            ItemApiTestUtils.read(CACHE_STRATEGY, itemId);
        }
    }
}
