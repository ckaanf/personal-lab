package ckaanf.cache.service.strategy.applicationlevelshardingreplication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ShardedKeyGenerator {

    public List<String> genShardedKeys(String key, int shardCount) {
        return IntStream.range(0, shardCount)
                .mapToObj(shardIndex -> genShardedKey(key, shardIndex))
                .toList();
    }

    public String findRandomShardedKey(String key, int shardCount) {
        return genShardedKey(key, RandomGenerator.getDefault().nextInt(shardCount));
    }

    private String genShardedKey(String key, int shardIndex) {
        return key + ":" + shardIndex;
    }
}
