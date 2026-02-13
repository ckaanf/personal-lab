package ckaanf.cache.service.strategy.splitbloomfilter;

import ckaanf.cache.service.strategy.bloomfilter.BloomFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SplitBloomFilter {
    private String id;
    private BloomFilter bloomFilter;
    private long splitCount;

    public static final long BIT_SPLIT_UNIT = 1L << 10;

    public static SplitBloomFilter create(String id, long dataCount, double falsePositiveRate) {
        BloomFilter bloomFilter = BloomFilter.create(id, dataCount, falsePositiveRate);
        long splitCount = (bloomFilter.getBitSize() - 1) / BIT_SPLIT_UNIT + 1;

        SplitBloomFilter splitBloomFilter = new SplitBloomFilter();
        splitBloomFilter.id = id;
        splitBloomFilter.bloomFilter = bloomFilter;
        splitBloomFilter.splitCount = splitCount;
        return splitBloomFilter;
    }

    public long findSplitIndex(Long hashedIndex) {
        if (hashedIndex >= bloomFilter.getBitSize()) {
            throw new IllegalArgumentException("hashedIndex must be less than bloomFilter.getBitSize()");
        }
        return hashedIndex / BIT_SPLIT_UNIT;
    }

    public long calSplitBitSize(long splitIndex) {
        if (splitIndex == splitCount - 1) {
            return bloomFilter.getBitSize() - (BIT_SPLIT_UNIT * splitIndex);
        }
        return BIT_SPLIT_UNIT;
    }
}
