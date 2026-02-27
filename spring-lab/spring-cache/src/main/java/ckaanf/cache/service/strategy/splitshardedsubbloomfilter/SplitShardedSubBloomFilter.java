package ckaanf.cache.service.strategy.splitshardedsubbloomfilter;

import ckaanf.cache.service.strategy.splitshardedbloomfilter.SplitShardedBloomFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SplitShardedSubBloomFilter {
    private String id;
    private SplitShardedBloomFilter splitShardedBloomFilter;

    public static SplitShardedSubBloomFilter create(String id, long dataCount, double falsePositiveRate, int shardCount) {
        SplitShardedSubBloomFilter splitShardedSubBloomFilter = new SplitShardedSubBloomFilter();
        splitShardedSubBloomFilter.id = id;
        splitShardedSubBloomFilter.splitShardedBloomFilter = SplitShardedBloomFilter.create(id, dataCount, falsePositiveRate, shardCount);
        return splitShardedSubBloomFilter;
    }

    public SplitShardedBloomFilter findSubFilter(int subFilterIndex) {
        return SplitShardedBloomFilter.create(
                id + ":sub:" + subFilterIndex,
                splitShardedBloomFilter.getDataCount() * (1L << (subFilterIndex + 1)),
                splitShardedBloomFilter.getFalsePositiveRate() / (1L << (subFilterIndex + 1)),
                splitShardedBloomFilter.getShardCount()
        );
    }

    public SplitShardedBloomFilter findActivatedFilter(int subFilterCount) {
        if (subFilterCount == 0) {
            return splitShardedBloomFilter;
        }

        int activatedSubFilterIndex = subFilterCount - 1;
        return findSubFilter(activatedSubFilterIndex);
    }

    public List<SplitShardedBloomFilter> findAll(int subFilterCount) {
        List<SplitShardedBloomFilter> splitShardedBloomFilters = new ArrayList<>();
        splitShardedBloomFilters.add(splitShardedBloomFilter);
        for (int subFilterIndex = 0; subFilterIndex < subFilterCount; subFilterIndex++) {
            splitShardedBloomFilters.add(findSubFilter(subFilterIndex));
        }
        return splitShardedBloomFilters;
    }
}
