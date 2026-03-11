package ckaanf.cache.service.strategy.bloomfilter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BloomFilterRedisHandler {
    private final StringRedisTemplate redisTemplate;

    public void init(BloomFilter bloomFilter) {
        String key = genKey(bloomFilter);
        for (long offset = 0; offset < bloomFilter.getBitSize(); offset+= 8L * 1024 * 1024) {
            redisTemplate.opsForValue().setBit(key, offset, false);
        }
    }

    public void add(BloomFilter bloomFilter, String value) {
        redisTemplate.executePipelined((RedisCallback<?>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;
            String key = genKey(bloomFilter);
            List<Long> hashedIndexes = bloomFilter.hash(value);
            for (Long hashedIndex : hashedIndexes) {
                conn.setBit(key, hashedIndex, true);
            }
            return null;
        });
    }

    public boolean mightContain(BloomFilter bloomFilter, String value) {
        return redisTemplate.executePipelined((RedisCallback<?>) action -> {
                    StringRedisConnection conn = (StringRedisConnection) action;
                    String key = genKey(bloomFilter);
                    List<Long> hashedIndexes = bloomFilter.hash(value);
                    for (Long hashedIndex : hashedIndexes) {
                        conn.getBit(key, hashedIndex);
                    }
                    return null;
                })
                .stream()
                .map(Boolean.class::cast)
                .allMatch(Boolean.TRUE::equals);
    }

    public void delete(BloomFilter bloomFilter) {
        redisTemplate.delete(genKey(bloomFilter));
    }

    private String genKey(BloomFilter bloomFilter) {
        return genKey(bloomFilter.getId());
    }

    private String genKey(String id) {
        return "bloom-filter:%s".formatted(id);
    }
}
