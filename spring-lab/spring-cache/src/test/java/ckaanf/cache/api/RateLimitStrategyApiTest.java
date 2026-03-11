package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitStrategyApiTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.RATE_LIMIT;

    @Test
    void test() throws InterruptedException {
        ItemResponse item = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data"));

        ExecutorService executorService = Executors.newFixedThreadPool(100);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(200);

        for (int i = 0; i < 200; i++) {
            executorService.execute(() -> {
                try {
                    ItemApiTestUtils.read(CACHE_STRATEGY, item.itemId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("successCount = " + successCount.get());
        System.out.println("failureCount = " + failureCount.get());

    }
}
