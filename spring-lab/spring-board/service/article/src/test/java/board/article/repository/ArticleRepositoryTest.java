package board.article.repository;

import board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findAllTest() {
        List<Article> articles = articleRepository.findAll(1L, 1499970L, 30L);
        log.info("article.size = {}", articles.size());
        for(Article article : articles) {
            log.info("article = {}", article);
        }
    }

    @Test
    void countTest() {
        Long count = articleRepository.count(1L, 10000L);
        log.info("count = {}", count);
    }

    @Test
    void findAllInfiniteScrollTest() {
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 30L);
        for(Article article : articles) {
            log.info("article = {}", article.getArticleId());
        }

        Long lastArticleId = articles.getLast().getArticleId();
        List<Article> articles2 = articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId);
        for(Article article : articles2) {
            log.info("article = {}", article.getArticleId());
        }
    }
}