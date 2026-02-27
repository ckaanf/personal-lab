package ckaanf.cache.service.strategy.splitshardedsubbloomfilter;

import ckaanf.cache.service.strategy.splitshardedbloomfilter.SplitShardedBloomFilter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SplitShardedSubBloomFilterTest {

    @Test
    void create() {
        SplitShardedSubBloomFilter splitShardedSubBloomFilter = SplitShardedSubBloomFilter.create(
                "testId", 1000, 0.01, 4);
        System.out.println("splitShardedSubBloomFilter = " + splitShardedSubBloomFilter);
        assertThat(splitShardedSubBloomFilter.getId()).isEqualTo("testId");
    }

    @Test
    void findSubFilter() {
        SplitShardedSubBloomFilter splitShardedSubBloomFilter = SplitShardedSubBloomFilter.create(
                "testId", 1000, 0.01, 4);

        SplitShardedBloomFilter subFilter0 = splitShardedSubBloomFilter.findSubFilter(0);
        SplitShardedBloomFilter subFilter1 = splitShardedSubBloomFilter.findSubFilter(1);
        SplitShardedBloomFilter subFilter2 = splitShardedSubBloomFilter.findSubFilter(2);

        System.out.println("subFilter0 = " + subFilter0);
        System.out.println("subFilter1 = " + subFilter1);
        System.out.println("subFilter2 = " + subFilter2);

        assertThat(subFilter0.getId()).isEqualTo(splitShardedSubBloomFilter.getId() + ":sub:0");
        assertThat(subFilter0.getDataCount()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getDataCount() * 2);
        assertThat(subFilter0.getFalsePositiveRate()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getFalsePositiveRate() / 2);
        assertThat(subFilter0.getShardCount()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getShardCount());

        assertThat(subFilter1.getId()).isEqualTo(splitShardedSubBloomFilter.getId() + ":sub:1");
        assertThat(subFilter1.getDataCount()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getDataCount() * 4);
        assertThat(subFilter1.getFalsePositiveRate()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getFalsePositiveRate() / 4);
        assertThat(subFilter1.getShardCount()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getShardCount());

        assertThat(subFilter2.getId()).isEqualTo(splitShardedSubBloomFilter.getId() + ":sub:2");
        assertThat(subFilter2.getDataCount()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getDataCount() * 8);
        assertThat(subFilter2.getFalsePositiveRate()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getFalsePositiveRate() / 8);
        assertThat(subFilter2.getShardCount()).isEqualTo(splitShardedSubBloomFilter.getSplitShardedBloomFilter().getShardCount());
    }

    @Test
    void findActivatedFilter_shouldReturnOriginFilter_whenSubFilterNotExists() {
        SplitShardedSubBloomFilter splitShardedSubBloomFilter = SplitShardedSubBloomFilter.create(
                "testId", 1000, 0.01, 4);

        SplitShardedBloomFilter activatedFilter = splitShardedSubBloomFilter.findActivatedFilter(0);

        assertThat(activatedFilter.getId()).isEqualTo(splitShardedSubBloomFilter.getId());
    }

    @Test
    void findActivatedFilter_shouldReturnSubFilter_whenSubFilterExists() {
        SplitShardedSubBloomFilter splitShardedSubBloomFilter = SplitShardedSubBloomFilter.create(
                "testId", 1000, 0.01, 4);


        int subFilterCount = 3;

        SplitShardedBloomFilter activatedFilter = splitShardedSubBloomFilter.findActivatedFilter(subFilterCount);

        assertThat(activatedFilter.getId()).isEqualTo(splitShardedSubBloomFilter.getId() + ":sub:2");
    }

    @Test
    void findAll() {
        SplitShardedSubBloomFilter splitShardedSubBloomFilter = SplitShardedSubBloomFilter.create(
                "testId", 1000, 0.01, 4);

        int subFilterCount = 3;

        List<SplitShardedBloomFilter> allFilters = splitShardedSubBloomFilter.findAll(subFilterCount);

        assertThat(allFilters.size()).isEqualTo(subFilterCount + 1);
        assertThat(allFilters.getFirst().getId()).isEqualTo(splitShardedSubBloomFilter.getId());

        for (int subFilterIndex = 0; subFilterIndex < subFilterCount; subFilterIndex++) {
            SplitShardedBloomFilter subFilter = allFilters.get(subFilterIndex + 1);
            assertThat(subFilter.getId()).isEqualTo(splitShardedSubBloomFilter.getId() + ":sub:" + subFilterIndex);
        }
    }

}