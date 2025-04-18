package board.articleread.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ViewClientTest {
    @Autowired
    ViewClient viewClient;

    @Test
    void readCacheableTest() throws InterruptedException {
        viewClient.count(1L);
        viewClient.count(1L);
        viewClient.count(1L);

        TimeUnit.SECONDS.sleep(3);
        viewClient.count(1L);
    }

}