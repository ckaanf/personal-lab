package ckaanf.cache.service.strategy.per;

import ckaanf.cache.common.serde.DataSerializer;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.random.RandomGenerator;

@Getter
@ToString
public class CacheData {
    private String data;
    private long computationTImeMillis;
    private long expiredAtMillis;

    public static CacheData of(Object data, long computationTImeMillis, Duration ttl) {
        CacheData cacheData = new CacheData();
        cacheData.data = DataSerializer.serializeOrException(data);
        cacheData.computationTImeMillis = computationTImeMillis;
        cacheData.expiredAtMillis = Instant.now().plus(ttl).toEpochMilli();
        return cacheData;
    }

    public <T> T parseData(Class<T> dataType) {
        return DataSerializer.deserializeOrNull(data, dataType);
    }

    public boolean shouldRecompute(double beta) {
        long nowMillis = Instant.now().toEpochMilli();
        double rand = RandomGenerator.getDefault().nextDouble();
        return nowMillis - computationTImeMillis * beta * Math.log(rand) >= expiredAtMillis;
    }
}
