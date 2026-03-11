package ckaanf.cache.service.strategy.splitshardedbloomfilter;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SplitShardedBloomFilterRedisHandlerTest extends RedisTestContainerSupport {

    @Autowired
    SplitShardedBloomFilterRedisHandler splitShardedBloomFilterRedisHandler;

    @Test
    void mightContain() {
        SplitShardedBloomFilter splitShardedBloomFilter = SplitShardedBloomFilter.create("testId", 1000, 0.01, 4);

        List<String> values = IntStream.range(0, 1000).mapToObj(i -> "value" + i).toList();
        for (String value : values) {
            splitShardedBloomFilterRedisHandler.add(splitShardedBloomFilter, value);
        }

        for (String value : values) {
            boolean result = splitShardedBloomFilterRedisHandler.mightContain(splitShardedBloomFilter, value);
            assertThat(result).isTrue();
        }

        for (int i = 0; i < 10000; i++) {
            String value = "notAddedValue" + i;
            boolean result = splitShardedBloomFilterRedisHandler.mightContain(splitShardedBloomFilter, value);

            if(result) {
                System.out.println("value = " + value);
            }
        }
    }

}