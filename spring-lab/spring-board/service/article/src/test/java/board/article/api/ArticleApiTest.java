package board.article.api;

import board.article.service.response.ArticlePageResponse;
import board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000");

    @Test
    void createTest() {
        ArticleResponse response = create(new ArticleCreateRequest("title", "content", 1L, 1L));
        System.out.println("response = " + response);
    }

    @Test
    void readTest() {
        Long articleId = 1L;
        ArticleResponse response = read(articleId);
        System.out.println("response = " + response);
    }


    @Test
    void updateTest() {
        ArticleResponse response = update();
        System.out.println("response = " + response);
    }


    @Test
    void deleteTest() {
        Long articleId = 1L;
        delete(articleId);
        System.out.println("Article deleted successfully");
    }

    @Test
    void readAllTest() {
        ArticlePageResponse response = restClient.get().uri("/api/v1/articles?boardId=1&pageSize=30&page=50000")
                .retrieve()
                .body(ArticlePageResponse.class);

        assert response != null;
        System.out.println("response.getArticleCount() = " + response.getArticleCount());
        for (ArticleResponse article : response.getArticles()) {
            System.out.println("article = " + article.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleResponse> response = restClient.get().uri("/api/v1/articles/infinite-scroll?boardId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });
        System.out.println("firstPage");

        assert response != null;
        for (ArticleResponse article : response) {
            System.out.println("article = " + article.getArticleId());
        }

        System.out.println("secondPage");
        Long lastArticleId = response.getLast().getArticleId();
        List<ArticleResponse> response2 = restClient.get().uri("/api/v1/articles/infinite-scroll?boardId=1&pageSize=5" +
                        "&lastArticleId=%s".formatted(lastArticleId) )
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleResponse>>() {
                });

        assert response2 != null;
        for (ArticleResponse article : response2) {
            System.out.println("article = " + article.getArticleId());
        }
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/api/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    private ArticleResponse read(Long articleId) {
        ArticleResponse response = restClient.get()
                .uri("/api/v1/articles/" + articleId)
                .retrieve()
                .body(ArticleResponse.class);
        return response;
    }

    private ArticleResponse update() {
        Long articleId = 1L;
        ArticleUpdateRequest request = new ArticleUpdateRequest("updated title", "updated content");
        ArticleResponse response = restClient.put()
                .uri("/api/v1/articles/" + articleId)
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
        return response;
    }

    private void delete(Long articleId) {
        restClient.delete()
                .uri("/api/v1/articles/" + articleId)
                .retrieve()
                .body(Void.class);
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class ArticleUpdateRequest {
        private String title;
        private String content;
    }
}
