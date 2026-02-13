package ckaanf.cache.service.strategy.splitbloomfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.LongStream;

@Component
@RequiredArgsConstructor
public class SplitBloomFilterRedisHandler {
    private final StringRedisTemplate redisTemplate;

    public void init(SplitBloomFilter splitBloomFilter) {
        for (long splitIndex = 0; splitIndex < splitBloomFilter.getSplitCount(); splitIndex++) {
            String key = genKey(splitBloomFilter, splitIndex);
            long bitSize = splitBloomFilter.calSplitBitSize(splitIndex);
            for (long offset = 0; offset < bitSize; offset += 8L * 1024 * 1024) {
                redisTemplate.opsForValue().setBit(key, offset, false);
            }
        }
    }

    public void add(SplitBloomFilter splitBloomFilter, String value) {
        redisTemplate.executePipelined((RedisCallback<Void>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;
            List<Long> hashedIndexes = splitBloomFilter.getBloomFilter().hash(value);

            for (Long hashedIndex : hashedIndexes) {
                long splitIndex = splitBloomFilter.findSplitIndex(hashedIndex);
                conn.setBit(genKey(splitBloomFilter, splitIndex), hashedIndex % SplitBloomFilter.BIT_SPLIT_UNIT, true);
            }

            return null;
        });
    }

    public boolean mightContain(SplitBloomFilter splitBloomFilter, String value) {
        return redisTemplate.executePipelined((RedisCallback<?>) action -> {
                    StringRedisConnection conn = (StringRedisConnection) action;
                    List<Long> hashedIndexes = splitBloomFilter.getBloomFilter().hash(value);
                    for (Long hashedIndex : hashedIndexes) {
                        long splitIndex = splitBloomFilter.findSplitIndex(hashedIndex);
                        conn.getBit(
                                genKey(splitBloomFilter, splitIndex),
                                hashedIndex % SplitBloomFilter.BIT_SPLIT_UNIT);
                    }
                    return null;
                })
                .stream()
                .map(Boolean.class::cast)
                .allMatch(Boolean.TRUE::equals);
    }

    public void delete(SplitBloomFilter splitBloomFilter) {
        redisTemplate.executePipelined((RedisCallback<?>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;
            genKeys(splitBloomFilter).forEach(conn::del);
            return null;
        });
    }

    private List<String> genKeys(SplitBloomFilter splitBloomFilter) {
        return LongStream.range(0, splitBloomFilter.getSplitCount())
                .mapToObj(splitIndex -> genKey(splitBloomFilter, splitIndex))
                .toList();
    }

    private String genKey(SplitBloomFilter splitBloomFilter, long splitIndex) {
        return "split-bloom-filter:%s:split:%s".formatted(splitBloomFilter.getId(), splitIndex);
    }

}
