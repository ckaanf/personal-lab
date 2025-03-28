package board.article.api;

import board.article.service.response.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

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
