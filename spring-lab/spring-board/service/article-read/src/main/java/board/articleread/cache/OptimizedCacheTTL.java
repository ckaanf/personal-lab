package board.articleread.cache;

import lombok.Getter;

import java.time.Duration;

@Getter
public class OptimizedCacheTTL {
    private Duration logicalTTL;
    private Duration physicalTTL;

    public static final long PHYSICAL_TTL_DELAY_SECONDS = 5;

    public static OptimizedCacheTTL of(long ttlSeconds) {
        OptimizedCacheTTL cacheTTL = new OptimizedCacheTTL();
        cacheTTL.logicalTTL = Duration.ofSeconds(ttlSeconds);
        cacheTTL.physicalTTL = cacheTTL.logicalTTL.plusSeconds(PHYSICAL_TTL_DELAY_SECONDS);
        return cacheTTL;
    }
}
