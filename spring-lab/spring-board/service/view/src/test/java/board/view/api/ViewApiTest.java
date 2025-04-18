package board.view.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewApiTest {
    RestClient restClient = RestClient.create("http://localhost:9003");


    @Test
    void viewTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(10000);

        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                restClient.post()
                        .uri("/api/v1/article-views/articles/{articleId}/users/{userId}", 4L, 1L)
                        .retrieve()
                        .body(Long.class);
                latch.countDown();
            });
        }
        latch.await();

        Long count = restClient.get().uri("/api/v1/article-views/articles/{articleId}/count", 4L)
                .retrieve()
                .body(Long.class);

        System.out.println("count = " + count);
    }

}
