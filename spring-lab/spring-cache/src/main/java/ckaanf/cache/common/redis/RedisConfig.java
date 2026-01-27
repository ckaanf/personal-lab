package ckaanf.cache.common.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void clearRedisOnStartup() {
        assert redisTemplate.getConnectionFactory() != null;

        // 학습용 캐시 클리어
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushDb();
    }
}
