package ckaanf.cache.service.strategy.writethrough;

import ckaanf.cache.common.serde.DataSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final StringRedisTemplate stringRedisTemplate;

    private static final long LIST_LIMIT = 100;
    private final RedisTemplate<Object, Object> redisTemplate;

    public void add(String listId, String id, Object data, Duration ttl, long score) {
        redisTemplate.executePipelined((RedisCallback<?>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;

            String key = genKey(id);
            String listKey = genListKey(listId);
            conn.set(
                    key,
                    DataSerializer.serializeOrException(data),
                    Expiration.from(ttl),
                    RedisStringCommands.SetOption.UPSERT
            );

            conn.zAdd(listKey, score, id);
            conn.zRemRange(listKey, 0, -LIST_LIMIT - 1);

            return null;
        });
    }

    public void del(String listId, String id) {
        redisTemplate.executePipelined((RedisCallback<?>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;

            conn.zRem(genListKey(listId), id);
            conn.del(genKey(id));
            return null;
        });
    }

    public <T> T read(String id, Class<T> clazz) {
        String result = redisTemplate.opsForValue().get(genKey(id)).toString();
        if (result == null) {
            return null;
        }
        return DataSerializer.deserializeOrNull(result, clazz);
    }

    public <T> List<T> readAll(String listId, long page, long pageSize, Class<T> clazz) {
        long start = (page -1) * pageSize;
        return redisTemplate.opsForZSet().reverseRange(genListKey(listId), start, start + pageSize - 1).stream()
                .map(id -> read((String) id, clazz))
                .toList();
    }

    public <T> List<T> readAllInfiniteScroll(String listId, Long lastScore, long pageSize, Class<T> clazz) {
        double min = Double.NEGATIVE_INFINITY;
        double max = lastScore != null ? lastScore - 1 : Double.POSITIVE_INFINITY;

        return redisTemplate.opsForZSet().reverseRangeByScore(genListKey(listId), min, max, 0, pageSize).stream()
                .map(id -> read((String) id, clazz))
                .toList();
    }

    private String genKey(String id) {
        return "repo:%s".formatted(id);
    }

    private String genListKey(String id) {
        return "repo:list:%s".formatted(id);
    }
}
