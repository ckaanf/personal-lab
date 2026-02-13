package ckaanf.cache.service.strategy.splitbloomfilter;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SplitBloomFilterRedisHandlerTest extends RedisTestContainerSupport {
    @Autowired
    SplitBloomFilterRedisHandler splitBloomFilterRedisHandler;

    @Test
    void mightContain() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);

        List<String> values = IntStream.range(0, 1000).mapToObj(idx -> "value" + idx).toList();
        for (String value : values) {
            splitBloomFilterRedisHandler.add(splitBloomFilter, value);
        }

        for (String value : values) {
            boolean result = splitBloomFilterRedisHandler.mightContain(splitBloomFilter, value);
            assertThat(result).isTrue();
        }

        for (int i = 0; i < 10000; i++) {
            String value = "notAddedValue" + i;
            boolean result = splitBloomFilterRedisHandler.mightContain(splitBloomFilter, value);
            if (result) {
                System.out.println("value = " + value);
            }
        }
    }
}