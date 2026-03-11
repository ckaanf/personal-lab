package ckaanf.cache.service.strategy.bloomfilter;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BloomFilterRedisHandlerTest extends RedisTestContainerSupport {
    @Autowired
    BloomFilterRedisHandler bloomFilterRedisHandler;

    @Test
    void add() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 1000, 0.01);

        bloomFilterRedisHandler.add(bloomFilter, "value");

        List<Long> hashedIndexes = bloomFilter.hash("value");
        for (long offset = 0; offset < bloomFilter.getBitSize(); offset++) {
            Boolean result = redisTemplate.opsForValue().getBit("bloom-filter:" + bloomFilter.getId(), offset);
            assertThat(result).isEqualTo(hashedIndexes.contains(offset));
        }
    }

    @Test
    void delete() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 1000, 0.01);
        bloomFilterRedisHandler.add(bloomFilter, "value");

        bloomFilterRedisHandler.delete(bloomFilter);

        for (long offset = 0; offset < bloomFilter.getBitSize(); offset++) {
            Boolean result = redisTemplate.opsForValue().getBit("bloom-filter:" + bloomFilter.getId(), offset);
            assertThat(result).isFalse();
        }
    }

    @Test
    void mightContain() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 1000, 0.01);

        List<String> values = IntStream.range(0, 1000).mapToObj(idx -> "value" + idx).toList();

        for (String value : values) {
            bloomFilterRedisHandler.add(bloomFilter, value);
        }

        for (String value : values) {
            boolean result = bloomFilterRedisHandler.mightContain(bloomFilter, value);
            assertThat(result).isTrue();
        }

        for (int i=0; i<10000; i++) {
            String value = "notAddedValue" + i;
            boolean result = bloomFilterRedisHandler.mightContain(bloomFilter, value);
            if (result) {
                System.out.println("value = " + value);
            }
        }
    }

    @Test
    void printExecutionTime_addToLargeBloomFilter() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 400_000_000, 0.01);
        List<Long> hashedIndexes = bloomFilter.hash("value");
        System.out.println("bloomFilter.getBitSize() = " + bloomFilter.getBitSize());
        System.out.println("hashedIndexes = " + hashedIndexes);

        long start = System.nanoTime();
        bloomFilterRedisHandler.add(bloomFilter, "value");
        long timeMillis = Duration.ofNanos(System.nanoTime() - start).toMillis();
        System.out.println("timeMillis = " + timeMillis);
    }

    @Test
    void printExecutionTime_addToLargeBloomFilterAfterInit() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 400_000_000, 0.01);
        List<Long> hashedIndexes = bloomFilter.hash("value");
        System.out.println("bloomFilter.getBitSize() = " + bloomFilter.getBitSize());
        System.out.println("hashedIndexes = " + hashedIndexes);

        bloomFilterRedisHandler.init(bloomFilter);

        long start = System.nanoTime();
        bloomFilterRedisHandler.add(bloomFilter, "value");
        long timeMillis = Duration.ofNanos(System.nanoTime() - start).toMillis();
        System.out.println("timeMillis = " + timeMillis);
    }

    @Test
    void mightContain_whenBloomFilterAddedToManyData() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 1000, 0.01);

        List<String> values = IntStream.range(0, 2000).mapToObj(idx -> "value" + idx).toList();

        for (String value : values) {
            bloomFilterRedisHandler.add(bloomFilter, value);
        }

        for (String value : values) {
            boolean result = bloomFilterRedisHandler.mightContain(bloomFilter, value);
            assertThat(result).isTrue();
        }

        for (int i=0; i<10000; i++) {
            String value = "notAddedValue" + i;
            boolean result = bloomFilterRedisHandler.mightContain(bloomFilter, value);
            if (result) {
                System.out.println("value = " + value);
            }
        }
    }
}