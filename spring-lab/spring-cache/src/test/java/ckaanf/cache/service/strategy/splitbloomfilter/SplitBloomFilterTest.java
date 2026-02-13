package ckaanf.cache.service.strategy.splitbloomfilter;

import org.junit.jupiter.api.Test;

import static ckaanf.cache.service.strategy.splitbloomfilter.SplitBloomFilter.BIT_SPLIT_UNIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SplitBloomFilterTest {

    @Test
    void create() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);
        System.out.println("splitBloomFilter = " + splitBloomFilter);

        long bitSize = splitBloomFilter.getBloomFilter().getBitSize();
        System.out.println("bitSize = " + bitSize);

        long splitCount = splitBloomFilter.getSplitCount();
        assertThat(splitCount).isEqualTo(10);
    }

    @Test
    void findSplitIndex() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);

        assertThat(splitBloomFilter.findSplitIndex(0L)).isEqualTo(0);
        assertThat(splitBloomFilter.findSplitIndex(1023L)).isEqualTo(0);
        assertThat(splitBloomFilter.findSplitIndex(1024L)).isEqualTo(1);
        assertThat(splitBloomFilter.findSplitIndex(9585L)).isEqualTo(9);
    }

    @Test
    void findSplitIndex_shouldThrowException_WhenHashedIndexExceedsBitSize() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);

        assertThatThrownBy(() -> splitBloomFilter.findSplitIndex(9586L)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void calSplitBitSize() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);

        long splitCount = splitBloomFilter.getSplitCount();
        for (long splitIndex = 0; splitIndex < splitCount - 1; splitIndex++) {
            assertThat(splitBloomFilter.calSplitBitSize(splitIndex)).isEqualTo(BIT_SPLIT_UNIT);
        }

        long bitSize = splitBloomFilter.getBloomFilter().getBitSize();
        assertThat(splitBloomFilter.calSplitBitSize(splitCount - 1)).isEqualTo(bitSize - BIT_SPLIT_UNIT * (splitCount - 1));
    }
}