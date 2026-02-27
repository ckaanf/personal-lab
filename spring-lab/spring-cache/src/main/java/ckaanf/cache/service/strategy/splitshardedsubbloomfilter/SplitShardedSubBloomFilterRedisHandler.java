package ckaanf.cache.service.strategy.splitshardedsubbloomfilter;

import ckaanf.cache.common.distributedlock.DistributedLockProvider;
import ckaanf.cache.service.strategy.splitshardedbloomfilter.SplitShardedBloomFilter;
import ckaanf.cache.service.strategy.splitshardedbloomfilter.SplitShardedBloomFilterRedisHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SplitShardedSubBloomFilterRedisHandler {
    private final StringRedisTemplate redisTemplate;
    private final DistributedLockProvider distributedLockProvider;
    private final SplitShardedBloomFilterRedisHandler splitShardedBloomFilterRedisHandler;

    public static final int MAX_SUB_FILTER_COUNT = 2;

    public void init(SplitShardedSubBloomFilter splitShardedSubBloomFilter) {
        SplitShardedBloomFilter splitShardedBloomFilter = splitShardedSubBloomFilter.getSplitShardedBloomFilter();
        splitShardedBloomFilterRedisHandler.init(splitShardedBloomFilter);
    }

    public void add(SplitShardedSubBloomFilter splitShardedSubBloomFilter, String value) {
        int subFilterCount = findSubFilterCount(splitShardedSubBloomFilter);
        SplitShardedBloomFilter activated = splitShardedSubBloomFilter.findActivatedFilter(subFilterCount);
        splitShardedBloomFilterRedisHandler.add(activated, value);
        Long dataCount = redisTemplate.opsForValue().increment(genDataCountKey(activated));
        appendSubFilterIfFull(splitShardedSubBloomFilter, activated, dataCount);

    }

    private int findSubFilterCount(SplitShardedSubBloomFilter splitShardedSubBloomFilter) {
        String result = redisTemplate.opsForValue().get(genSubFilterCountKey(splitShardedSubBloomFilter));
        if (!StringUtils.hasText(result)) {
            return 0;
        }
        return Integer.parseInt(result);
    }

    private void appendSubFilterIfFull(SplitShardedSubBloomFilter splitShardedSubBloomFilter,
                                       SplitShardedBloomFilter activated, Long dataCount) {
        if (!isFull(activated, dataCount)) {
            return;
        }

        String distributedKey = genSubFilterCountDistributedLockKey(splitShardedSubBloomFilter);
        if (!distributedLockProvider.lock(distributedKey, Duration.ofMinutes(1))) {
            return;
        }

        try {
            int subFilterCount = findSubFilterCount(splitShardedSubBloomFilter);
            if (subFilterCount >= MAX_SUB_FILTER_COUNT) {
                log.warn("sub-filter limit reached. id = {}, subFilterCount = {}", splitShardedSubBloomFilter.getId(), subFilterCount);
                return;
            }

            splitShardedBloomFilterRedisHandler.init(splitShardedSubBloomFilter.findSubFilter(subFilterCount));
            redisTemplate.opsForValue().increment(genSubFilterCountKey(splitShardedSubBloomFilter));

        } finally {
            distributedLockProvider.unlock(distributedKey);
        }

    }

    public boolean mightContain(SplitShardedSubBloomFilter splitShardedSubBloomFilter, String value) {
        int subFilterCount = findSubFilterCount(splitShardedSubBloomFilter);
        return splitShardedSubBloomFilter.findAll(subFilterCount).stream()
                .anyMatch(s -> splitShardedBloomFilterRedisHandler.mightContain(s, value));
    }

    public void delete(SplitShardedSubBloomFilter splitShardedSubBloomFilter) {
        int subFilterCount = findSubFilterCount(splitShardedSubBloomFilter);
        List<SplitShardedBloomFilter> filters = splitShardedSubBloomFilter.findAll(subFilterCount);
        for (SplitShardedBloomFilter filter : filters) {
            splitShardedBloomFilterRedisHandler.delete(filter);
            redisTemplate.delete(genDataCountKey(filter));
        }
        redisTemplate.delete(genSubFilterCountKey(splitShardedSubBloomFilter));
    }

    private boolean isFull(SplitShardedBloomFilter activated, Long dataCount) {
        return dataCount != null && activated.getDataCount() <= dataCount;
    }

    private String genDataCountKey(SplitShardedBloomFilter splitShardedBloomFilter) {
        return "split-sharded-sub-bloom-filter:data-count:%s".formatted(splitShardedBloomFilter.getId());
    }

    private String genSubFilterCountKey(SplitShardedSubBloomFilter splitShardedSubBloomFilter) {
        return "split-sharded-sub-bloom-filter:sub-filter-count:%s".formatted(splitShardedSubBloomFilter.getId());
    }

    private String genSubFilterCountDistributedLockKey(SplitShardedSubBloomFilter splitShardedSubBloomFilter) {
        return "split-sharded-sub-bloom-filter:sub-filter-count:%s:distributed-lock".formatted(splitShardedSubBloomFilter.getId());
    }
}
