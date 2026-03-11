package ckaanf.cache.common.distributedlock;

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
class DistributedLockProviderTest extends RedisTestContainerSupport {

    @Autowired
    DistributedLockProvider distributedLockProvider;

    @Test
    void lock() throws InterruptedException {
        assertThat(distributedLockProvider.lock("testId", Duration.ofSeconds(1))).isTrue();
        assertThat(distributedLockProvider.lock("testId", Duration.ofSeconds(1))).isFalse();
        assertThat(distributedLockProvider.lock("testId", Duration.ofSeconds(1))).isFalse();

        TimeUnit.SECONDS.sleep(2);
        assertThat(distributedLockProvider.lock("testId", Duration.ofSeconds(1))).isTrue();
    }

    @Test
    void lock_shouldAcquireOnlyOnce_whenMultiThread() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                boolean locked = distributedLockProvider.lock("testId", Duration.ofSeconds(10));
                if (locked) {
                    count.incrementAndGet();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        assertThat(count.get()).isEqualTo(1);
    }

    @Test
    void unlock() {
        distributedLockProvider.lock("testId", Duration.ofSeconds(1));

        distributedLockProvider.unlock("testId");

        boolean locked = distributedLockProvider.lock("testId", Duration.ofSeconds(1));

        assertThat(locked).isTrue();

    }
}