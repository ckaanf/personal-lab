package ckaanf.cache.service.strategy.bloomfilter;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class BloomFilter {
    private String id;
    private long dataCount;
    private double falsePositiveRate;
    private long bitSize;
    private int hashFunctionCount;
    private List<BloomFilterHashFunction> hashFunctions;

    public static BloomFilter create(String id, long dataCount, double falsePositiveRate) {
        if (dataCount <= 0) {
            throw new IllegalArgumentException();
        }

        if (falsePositiveRate <= 0.0 || falsePositiveRate >= 1.0) {
            throw new IllegalArgumentException();
        }

        long bitSize = calculateBitSize(dataCount, falsePositiveRate);
        int hashFunctionCount = calculateHashFunctionCount(dataCount, bitSize);

        List<BloomFilterHashFunction> hashFunctions = IntStream.range(0, hashFunctionCount)
                .mapToObj(seed ->
                        (BloomFilterHashFunction) value -> Math.abs(Hashing.murmur3_128(seed)
                                .hashString(value, StandardCharsets.UTF_8)
                                .asLong() % bitSize
                        )
                )
                .toList();

        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.id = id;
        bloomFilter.dataCount = dataCount;
        bloomFilter.falsePositiveRate = falsePositiveRate;
        bloomFilter.bitSize = bitSize;
        bloomFilter.hashFunctionCount = hashFunctionCount;
        bloomFilter.hashFunctions = hashFunctions;

        return bloomFilter;
    }

    /**
     * - (n * ln(p)) / ln(2)^2
     */
    private static long calculateBitSize(long dataCount, double falsePositiveRate) {
        return (long) Math.ceil(
                -(dataCount * Math.log(falsePositiveRate)) / (Math.pow(Math.log(2),2))
        );
    }

    /**
     *  (m / n) * ln(2)
     */
    private static int calculateHashFunctionCount(long dataCount, long bitSize) {
        return (int) Math.ceil(
                (bitSize / (double) dataCount) * Math.log(2)
        );
    }

    public List<Long> hash(String value) {
        return hashFunctions.stream()
                .map(hashFunction -> hashFunction.hash(value))
                .collect(Collectors.toList());
    }
}
