package board.articleread.cache;

import board.common.dataserializer.DataSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@ToString
public class OptimizedCache {
    private String data;
    private LocalDateTime expiredAt;

    public static OptimizedCache of(Object data, Duration ttl) {
        OptimizedCache cache = new OptimizedCache();
        cache.data = DataSerializer.serialize(data);
        cache.expiredAt = LocalDateTime.now().plus(ttl);
        return cache;
    }

    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public <T> T parseData(Class<T> clazz) {
        return DataSerializer.deserialize(data, clazz);
    }
}
