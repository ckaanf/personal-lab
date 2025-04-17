package board.hotarticle.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HotArticleListRepositoryTest {

    @Autowired
    private HotArticleListRepository hotArticleListRepository;

    @Test
    void addTest() throws InterruptedException {
        // given
        LocalDateTime time = LocalDateTime.of(2025, 4, 14, 0, 0);
        long limit = 3;

        //when
        hotArticleListRepository.add(1L, time, 2L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(2L, time, 3L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(3L, time, 1L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(4L, time, 5L, limit, Duration.ofSeconds(3));
        hotArticleListRepository.add(5L, time, 4L, limit, Duration.ofSeconds(3));

        // then
        List<Long> articleIds = hotArticleListRepository.readAll("20250414");
        assertThat(articleIds).hasSize(3);
        assertThat(articleIds.getFirst()).isEqualTo(4L);
        assertThat(articleIds).containsExactly(4L, 5L, 2L);

        TimeUnit.SECONDS.sleep(5);
        assertThat(hotArticleListRepository.readAll("20250414")).isEmpty();
    }
}