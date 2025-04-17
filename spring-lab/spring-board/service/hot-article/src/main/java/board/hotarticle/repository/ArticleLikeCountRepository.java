package board.hotarticle.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleLikeCountRepository {
    private final StringRedisTemplate redisTemplate;

    // hot-article::article::{articleId}::like-count
    private static final String KEY_FORMAT = "hot-article::article::%s::like-count";

    public void createOrUpdate(Long articleId, Long likeCount, Duration ttl) {
        redisTemplate.opsForValue().set(generateKey(articleId), String.valueOf(likeCount), ttl);
    }

    public Long read(Long articleId) {
        String result = redisTemplate.opsForValue().get(generateKey(articleId));
        return result == null ? 0L : Long.valueOf(result);
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }
}
