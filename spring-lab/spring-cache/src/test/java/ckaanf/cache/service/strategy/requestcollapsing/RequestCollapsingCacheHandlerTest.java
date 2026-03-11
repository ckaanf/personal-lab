package ckaanf.cache.service.strategy.requestcollapsing;

import ckaanf.cache.RedisTestContainerSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RequestCollapsingCacheHandlerTest extends RedisTestContainerSupport {

    @Autowired
    RequestCollapsingCacheHandler handler;

    @Test
    void put() {
        handler.put("testKey", Duration.ofSeconds(10), "data");

        String result = redisTemplate.opsForValue().get("testKey");
        assertThat(result).isNotNull();
        System.out.println("result = " + result);
    }

    @Test
    void evict() {
        handler.put("testKey", Duration.ofSeconds(10), "data");

        handler.evict("testKey");

        String result = redisTemplate.opsForValue().get("testKey");
        assertThat(result).isNull();
    }

    @Test
    void fetch_shouldSupplySourceDataOnlyOnce_whenMultiThreadAndRefreshSucceeded() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        AtomicInteger dataSourceExecCount = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                String result = handler.fetch(
                        "testKey",
                        Duration.ofSeconds(10),
                        () -> {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException ignored) {
                            }
                            dataSourceExecCount.incrementAndGet();
                            return "sourceData";
                        },
                        String.class
                );

                System.out.println("result = " + result);
                assertThat(result).isEqualTo("sourceData");
                latch.countDown();
            });
        }
        latch.await();

        assertThat(dataSourceExecCount.get()).isEqualTo(1);
    }

    @Test
    void fetch_shouldRefreshDataSource_whenMultiThreadAndTimeOut() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        AtomicInteger dataSourceExecCount = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                String result = handler.fetch(
                        "testKey",
                        Duration.ofSeconds(10),
                        () -> {
                            try {
                                TimeUnit.SECONDS.sleep(3);
                            } catch (InterruptedException ignored) {
                            }
                            dataSourceExecCount.incrementAndGet();
                            return "sourceData";
                        },
                        String.class
                );

                System.out.println("result = " + result);
                assertThat(result).isEqualTo("sourceData");
                latch.countDown();
            });
        }
        latch.await();

        assertThat(dataSourceExecCount.get()).isEqualTo(10);
    }

}