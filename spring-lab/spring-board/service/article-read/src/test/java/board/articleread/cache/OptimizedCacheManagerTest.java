package board.articleread.cache;

import board.common.dataserializer.DataSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OptimizedCacheManagerTest {
    @InjectMocks
    OptimizedCacheManager optimizedCacheManager;
    @Mock
    StringRedisTemplate stringRedisTemplate;
    @Mock
    OptimizedCacheLockProvider optimizedCacheLockProvider;
    @Mock
    ValueOperations valueOperations = mock(ValueOperations.class);

    @BeforeEach
    void beforeEach() {
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
    }

    @Test
    @DisplayName("캐시 데이터가 없으면 원본 데이터 요청")
    void processShouldCallOriginDataIfCachedDataIsNull() throws Throwable {
        // given
        String type = "testType";
        long ttlSeconds = 10;
        Object[] args = new Object[]{1, "param"};
        Class<String> returnType = String.class;
        OptimizedCacheOriginDataSupplier<String> originDataSupplier = () -> "origin";

        String cachedData = null;
        given(valueOperations.get("testType::1::param")).willReturn(cachedData);

        // when
        Object result = optimizedCacheManager.process(type, ttlSeconds, args, returnType, originDataSupplier);

        // then
        assertThat(result).isEqualTo(originDataSupplier.get());
        verify(valueOperations).set(eq("testType::1::param"), anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("유효하지 않은 캐시 데이터라면 원본 데이터 요청")
    void processShouldCallOriginDataIfInvalidCachedData() throws Throwable {
        // given
        String type = "testType";
        long ttlSeconds = 10;
        Object[] args = new Object[]{1, "param"};
        Class<String> returnType = String.class;
        OptimizedCacheOriginDataSupplier<String> originDataSupplier = () -> "origin";

        String cachedData = "{::invalid";
        given(valueOperations.get("testType::1::param")).willReturn(cachedData);

        // when
        Object result = optimizedCacheManager.process(type, ttlSeconds, args, returnType, originDataSupplier);

        // then
        assertThat(result).isEqualTo(originDataSupplier.get());
        verify(valueOperations).set(eq("testType::1::param"), anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("논리적으로 만료되지 않은 데이터면 캐시 데이터 반환")
    void processShouldReturnCachedDataIfNotExpiredLogically() throws Throwable {
        // given
        String type = "testType";
        long ttlSeconds = 10;
        Object[] args = new Object[]{1, "param"};
        Class<String> returnType = String.class;
        OptimizedCacheOriginDataSupplier<String> originDataSupplier = () -> "origin";

        OptimizedCache optimizedCache = OptimizedCache.of("cached", Duration.ofSeconds(ttlSeconds));
        String cachedData = DataSerializer.serialize(optimizedCache);
        given(valueOperations.get("testType::1::param")).willReturn(cachedData);

        // when
        Object result = optimizedCacheManager.process(type, ttlSeconds, args, returnType, originDataSupplier);

        // then
        assertThat(result).isEqualTo("cached");
        verify(valueOperations, never()).set(eq("testType::1::param"), anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("논리적으로 만료된 데이터면 락 획득 시도. 락 실패 시 캐시 데이터 반환")
    void processShouldReturnCachedDataIfExpiredLogicallyAndLockNotAcquired() throws Throwable {
        // given
        String type = "testType";
        long ttlSeconds = 10;
        Object[] args = new Object[]{1, "param"};
        Class<String> returnType = String.class;
        OptimizedCacheOriginDataSupplier<String> originDataSupplier = () -> "origin";

        OptimizedCache optimizedCache = OptimizedCache.of("cached", Duration.ofSeconds(-1));
        String cachedData = DataSerializer.serialize(optimizedCache);
        given(valueOperations.get("testType::1::param")).willReturn(cachedData);

        given(optimizedCacheLockProvider.lock("testType::1::param")).willReturn(false);

        // when
        Object result = optimizedCacheManager.process(type, ttlSeconds, args, returnType, originDataSupplier);

        // then
        assertThat(result).isEqualTo("cached");
        verify(valueOperations, never()).set(eq("testType::1::param"), anyString(), any(Duration.class));
    }

    @Test
    @DisplayName("논리적으로 만료된 데이터면 락 획득 시도. 락 성공 시 캐시 리프레시 후 반환")
    void processShouldCallOriginDataAndRefreshCacheIfExpiredLogicallyAndLockAcquired() throws Throwable {
        // given
        String type = "testType";
        long ttlSeconds = 10;
        Object[] args = new Object[]{1, "param"};
        Class<String> returnType = String.class;
        OptimizedCacheOriginDataSupplier<String> originDataSupplier = () -> "origin";

        OptimizedCache optimizedCache = OptimizedCache.of("cached", Duration.ofSeconds(-1));
        String cachedData = DataSerializer.serialize(optimizedCache);
        given(valueOperations.get("testType::1::param")).willReturn(cachedData);

        given(optimizedCacheLockProvider.lock("testType::1::param")).willReturn(true);

        // when
        Object result = optimizedCacheManager.process(type, ttlSeconds, args, returnType, originDataSupplier);

        // then
        assertThat(result).isEqualTo("origin");
        verify(valueOperations).set(eq("testType::1::param"), anyString(), any(Duration.class));
    }
}