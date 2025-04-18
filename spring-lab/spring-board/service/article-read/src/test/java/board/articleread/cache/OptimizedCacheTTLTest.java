package board.articleread.cache;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimizedCacheTTLTest {

    @Test
    void ofTest() {
        // given
        long ttlSeconds = 10L;

        // when
        OptimizedCacheTTL cacheTTL = OptimizedCacheTTL.of(ttlSeconds);

        // then
        assertEquals(ttlSeconds, cacheTTL.getLogicalTTL().getSeconds());
        assertEquals(ttlSeconds + OptimizedCacheTTL.PHYSICAL_TTL_DELAY_SECONDS, cacheTTL.getPhysicalTTL().getSeconds());
    }
}
