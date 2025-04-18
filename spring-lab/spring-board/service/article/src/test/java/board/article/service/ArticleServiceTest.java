package board.article.service;

import board.article.entity.Article;
import board.article.repository.ArticleRepository;
import board.article.service.response.ArticlePageResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockitoBean
    private ArticleRepository articleRepository;

    @Test
    void testReadAll_ReturnsEmptyPageResponse_WhenNoArticlesExistForBoardId() {
        when(articleRepository.findAll(anyLong(), anyLong(), anyLong())).thenReturn(List.of());
        when(articleRepository.count(anyLong(), anyLong())).thenReturn(0L);

        ArticlePageResponse response = articleService.readAll(1L, 1L, 10L);

        assertEquals(0, response.getArticles().size());
        assertEquals(0, response.getArticleCount());
    }

    @Test
    void testReadAll_ReturnsCorrectPageResponse_WhenArticlesExist() {
        Article article1 = Mockito.mock(Article.class);
        Article article2 = Mockito.mock(Article.class);
        when(articleRepository.findAll(1L, 0L, 10L)).thenReturn(List.of(article1, article2));
        when(articleRepository.count(1L, 10L)).thenReturn(2L);

        ArticlePageResponse response = articleService.readAll(1L, 1L, 10L);

        assertEquals(2, response.getArticles().size());
        assertEquals(2, response.getArticleCount());
    }
}