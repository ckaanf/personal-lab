package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import org.junit.jupiter.api.Test;

public class NullObjectPatternStrategyApiTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.NULL_OBJECT_PATTERN;

    @Test
    void read() {
        for (int i = 0; i < 3; i++) {
            ItemApiTestUtils.read(CACHE_STRATEGY, 9999L);
            System.out.println("read " + i);
        }
    }
}
