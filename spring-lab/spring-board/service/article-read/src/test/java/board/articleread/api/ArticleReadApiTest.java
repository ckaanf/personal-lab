package board.articleread.api;

import board.articleread.service.response.ArticleReadPageResponse;
import board.articleread.service.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class ArticleReadApiTest {
    RestClient articleReadRestClient = RestClient.create("http://localhost:9005");
    RestClient articleRestClient = RestClient.create("http://localhost:9000");

    @Test
    void readTest() {
        ArticleReadResponse response = articleReadRestClient.get().uri("/api/v1/articles/{articleId}", 140625524952190976L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void readAll() {
        ArticleReadPageResponse response1 = articleReadRestClient.get().uri(("/api/v1/articles?boardId={boardId}&page={pageId}" +
                        "&pageSize={pageSize}"),1,3000,5)
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response1.getArticleCount() =" + response1.getArticleCount());
        for (ArticleReadResponse articleReadResponse : response1.getArticles()) {
            System.out.println("article.getArticleId() = " + articleReadResponse.getArticleId());
        }

        ArticleReadPageResponse response2 = articleRestClient.get().uri(("/api/v1/articles?boardId={boardId}&page={pageId}" +
                "&pageSize={pageSize}"),1,3000,5)
                .retrieve()
                .body(ArticleReadPageResponse.class);

        System.out.println("response2.getArticleCount() =" + response2.getArticleCount());
        for (ArticleReadResponse articleReadResponse : response2.getArticles()) {
            System.out.println("article.getArticleId() = " + articleReadResponse.getArticleId());
        }
    }

    @Test
    void readAllInfiniteScrollTest() {
        List<ArticleReadResponse> response1 = articleReadRestClient.get().uri("/api/v1/articles/infinite-scroll?boardId={boardId}&pageSize={pageSize}", 1, 5)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
                });

        for (ArticleReadResponse articleReadResponse : response1) {
            System.out.println("article.getArticleId() = " + articleReadResponse.getArticleId());
        }

        List<ArticleReadResponse> response2 = articleRestClient.get().uri("/api/v1/articles/infinite-scroll" +
                        "?boardId={boardId}&pageSize={pageSize}", 1, 5)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ArticleReadResponse>>() {
                });

        for (ArticleReadResponse articleReadResponse : response2) {
            System.out.println("article.getArticleId() = " + articleReadResponse.getArticleId());
        }
    }
}
