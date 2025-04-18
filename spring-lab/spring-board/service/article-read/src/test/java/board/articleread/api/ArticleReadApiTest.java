package board.articleread.api;

import board.articleread.service.response.ArticleReadResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleReadApiTest {
    RestClient restClient = RestClient.create("http://localhost:9005");

    @Test
    void readTest() {
        ArticleReadResponse response = restClient.get().uri("/api/v1/articles/{articleId}", 140625524952190976L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }
}
