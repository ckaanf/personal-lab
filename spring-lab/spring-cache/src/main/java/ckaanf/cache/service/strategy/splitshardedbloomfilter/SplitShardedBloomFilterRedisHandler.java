package ckaanf.cache.service.strategy.splitshardedbloomfilter;

import ckaanf.cache.service.strategy.splitbloomfilter.SplitBloomFilter;
import ckaanf.cache.service.strategy.splitbloomfilter.SplitBloomFilterRedisHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SplitShardedBloomFilterRedisHandler {
    private final SplitBloomFilterRedisHandler splitBloomFilterRedisHandler;

    public void init(SplitShardedBloomFilter splitShardedBloomFilter) {
        List<SplitBloomFilter> shards = splitShardedBloomFilter.getShards();
        shards.forEach(splitBloomFilterRedisHandler::init);
    }

    public void add(SplitShardedBloomFilter splitShardedBloomFilter, String value) {
        SplitBloomFilter shard = splitShardedBloomFilter.findShard(value);
        splitBloomFilterRedisHandler.add(shard, value);
    }

    public boolean mightContain(SplitShardedBloomFilter splitShardedBloomFilter, String value) {
        SplitBloomFilter shard = splitShardedBloomFilter.findShard(value);
        return splitBloomFilterRedisHandler.mightContain(shard, value);
    }

    public void delete(SplitShardedBloomFilter splitShardedBloomFilter) {
        List<SplitBloomFilter> shards = splitShardedBloomFilter.getShards();
        for (SplitBloomFilter shard : shards) {
            splitBloomFilterRedisHandler.delete(shard);
        }
    }
}
