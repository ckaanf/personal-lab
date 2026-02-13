package ckaanf.cache.service.strategy.splitshardedbloomfilter;

import ckaanf.cache.service.strategy.splitbloomfilter.SplitBloomFilter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SplitShardedBloomFilterTest {

    @Test
    void create() {
        SplitShardedBloomFilter splitShardedBloomFilter = SplitShardedBloomFilter.create(
                "testId", 1000, 0.01, 4
        );
        long dataCount = 0;
        List<SplitBloomFilter> shards = splitShardedBloomFilter.getShards();
        for(SplitBloomFilter shard : shards) {
            System.out.println("shard = " + shard);
            dataCount += shard.getBloomFilter().getDataCount();
        }

        assertThat(dataCount).isEqualTo(splitShardedBloomFilter.getDataCount());
    }

    @Test
    void findShard() {
        SplitShardedBloomFilter splitShardedBloomFilter = SplitShardedBloomFilter.create(
                "testId", 1000, 0.01, 4
        );

        SplitBloomFilter shard = splitShardedBloomFilter.findShard("value");
        System.out.println("shard = " + shard);

        assertThat(shard).isNotNull();
    }

}