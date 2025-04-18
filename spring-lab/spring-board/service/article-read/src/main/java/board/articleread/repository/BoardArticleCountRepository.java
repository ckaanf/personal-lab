package board.articleread.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardArticleCountRepository {
    private final StringRedisTemplate redisTemplate;

    // article-read::board-article-count::board::{boardId}
    private static final String KEY_FORMAT = "article-read::board-article-count::board::%s";

    public void createOrUpdate(Long boardId, Long articleCount) {
        redisTemplate.opsForValue().set(generateKey(boardId), String.valueOf(articleCount));
    }

    public Long read(Long boardId) {
        String result = redisTemplate.opsForValue().get(generateKey(boardId));
        return result == null ? 0L : Long.valueOf(result);
    }

    private String generateKey(Long boardId) {
        return KEY_FORMAT.formatted(boardId);
    }
}
