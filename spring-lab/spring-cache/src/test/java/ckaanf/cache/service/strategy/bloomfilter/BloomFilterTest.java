package ckaanf.cache.service.strategy.bloomfilter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BloomFilterTest {

    @Test
    void create() {
        BloomFilter bloomfilter1 = BloomFilter.create("testId1", 1000, 0.01);
        assertThat(bloomfilter1.getId()).isEqualTo("testId1");
        assertThat(bloomfilter1.getDataCount()).isEqualTo(1000);
        assertThat(bloomfilter1.getFalsePositiveRate()).isEqualTo(0.01);
        assertThat(bloomfilter1.getBitSize()).isEqualTo(9586);
        assertThat(bloomfilter1.getHashFunctionCount()).isEqualTo(7);
        System.out.println("bloomfilter1 = " + bloomfilter1);


        BloomFilter bloomfilter2 = BloomFilter.create("testId2", 100_000_000, 0.01);
        assertThat(bloomfilter2.getId()).isEqualTo("testId2");
        assertThat(bloomfilter2.getDataCount()).isEqualTo(100_000_000);
        assertThat(bloomfilter2.getFalsePositiveRate()).isEqualTo(0.01);
        assertThat(bloomfilter2.getBitSize()).isEqualTo(958_505_838);
        assertThat(bloomfilter2.getHashFunctionCount()).isEqualTo(7);

        System.out.println("bloomfilter2 = " + bloomfilter2);
    }

    @Test
    void hash() {
        BloomFilter bloomFilter = BloomFilter.create("testId", 1000, 0.01);
        for (int i = 0; i < 100; i++) {
            List<Long> hashedIndexes = bloomFilter.hash("value" + i);
            assertThat(hashedIndexes.size()).isEqualTo(bloomFilter.getHashFunctionCount());
            for (Long hashedIndex : hashedIndexes) {
                assertThat(hashedIndex).isGreaterThanOrEqualTo(0L);
                assertThat(hashedIndex).isLessThan(bloomFilter.getBitSize());
                System.out.println("hashedIndex = " + hashedIndex);
            }
            System.out.println();
        }
    }

}