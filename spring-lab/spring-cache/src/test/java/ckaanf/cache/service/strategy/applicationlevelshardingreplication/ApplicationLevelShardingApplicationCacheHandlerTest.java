package ckaanf.cache.service.strategy.applicationlevelshardingreplication;

import ckaanf.cache.RedisTestContainerSupport;
import ckaanf.cache.common.serde.DataSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationLevelShardingApplicationCacheHandlerTest extends RedisTestContainerSupport {

    @Autowired
    ApplicationLevelShardingApplicationCacheHandler cacheHandler;

    @Autowired
    ShardedKeyGenerator shardedKeyGenerator;


    @Test
    void put() {
        cacheHandler.put("testKey", Duration.ofSeconds(3), "data");

        List<String> shardedKeys = shardedKeyGenerator.genShardedKeys("testKey", 3);
        for (String shardedKey : shardedKeys) {
            String result = redisTemplate.opsForValue().get(shardedKey);
            assertThat(DataSerializer.deserializeOrNull(result, String.class)).isEqualTo("data");
        }

    }

    @Test
    void evict() {
        cacheHandler.put("testKey", Duration.ofSeconds(3), "data");

        cacheHandler.evict("testKey");

        List<String> shardedKeys = shardedKeyGenerator.genShardedKeys("testKey", 3);
        for (String shardedKey : shardedKeys) {
            String result = redisTemplate.opsForValue().get(shardedKey);
            assertThat(result).isNull();
        }
    }

    @Test
    void fetch() {
        String result1 = fetchData();
        String result2 = fetchData();
        String result3 = fetchData();

        assertThat(result1).isEqualTo("sourceData");
        assertThat(result2).isEqualTo("sourceData");
        assertThat(result3).isEqualTo("sourceData");
    }

    private String fetchData() {
        return cacheHandler.fetch(
                "testKey",
                Duration.ofSeconds(3),
                () -> {
                    System.out.println("fetch source data");
                    return "sourceData";
                },
                String.class
        );
    }
}