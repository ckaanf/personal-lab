package ckaanf.cache.service.strategy.writethrough;

import ckaanf.cache.RedisTestContainerSupport;
import ckaanf.cache.model.Item;
import ckaanf.cache.model.ItemCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisRepositoryTest extends RedisTestContainerSupport {

    @Autowired
    RedisRepository redisRepository;

    @Test
    void addAndDel() {
        Item item = Item.create(new ItemCreateRequest("data"));
        redisRepository.add(
                "itemList",
                String.valueOf(item.getItemId()),
                item,
                Duration.ofSeconds(10),
                item.getItemId()
        );

        Item result = redisRepository.read(String.valueOf(item.getItemId()), Item.class);
        assertThat(result.getItemId()).isEqualTo(item.getItemId());
        assertThat(result.getData()).isEqualTo(item.getData());

        redisRepository.del("itemList", String.valueOf(item.getItemId()));

        Item resultAfterDel = redisRepository.read(String.valueOf(item.getItemId()), Item.class);
        assertThat(resultAfterDel).isNull();
    }

    @Test
    void readAll() {
        List<Item> items = IntStream.range(0, 120)
                .mapToObj(i -> Item.create(new ItemCreateRequest("data" + i)))
                .toList();

        for (Item item : items) {
            redisRepository.add(
                    "itemList",
                    String.valueOf(item.getItemId()),
                    item,
                    Duration.ofSeconds(10),
                    item.getItemId()
            );
        }

        List<Item> items1 = redisRepository.readAll("itemList", 1, 40, Item.class);
        List<Item> items2 = redisRepository.readAll("itemList", 2, 40, Item.class);
        List<Item> items3 = redisRepository.readAll("itemList", 3, 40, Item.class);

        assertThat(items1).hasSize(40);
        assertThat(items2).hasSize(40);
        assertThat(items3).hasSize(20);

        for (int i = 0; i < 100; i++) {
            Item item = items.get(items.size() - 1 - i);
            if (i < 40) {
                assertThat(items1.get(i).getItemId()).isEqualTo(item.getItemId());
            } else if (i < 80) {
                assertThat(items2.get(i - 40).getItemId()).isEqualTo(item.getItemId());
            } else {
                assertThat(items3.get(i - 80).getItemId()).isEqualTo(item.getItemId());
            }
        }
    }

    @Test
    void readAllInfiniteScroll() {
        List<Item> items = IntStream.range(0, 120)
                .mapToObj(i -> Item.create(new ItemCreateRequest("data" + i)))
                .toList();

        for (Item item : items) {
            redisRepository.add(
                    "itemList",
                    String.valueOf(item.getItemId()),
                    item,
                    Duration.ofSeconds(10),
                    item.getItemId()
            );
        }

        List<Item> items1 = redisRepository.readAllInfiniteScroll("itemList", null, 40, Item.class);
        List<Item> items2 = redisRepository.readAllInfiniteScroll("itemList", items1.getLast().getItemId(), 40, Item.class);
        List<Item> items3 = redisRepository.readAllInfiniteScroll("itemList", items2.getLast().getItemId(), 40, Item.class);

        assertThat(items1).hasSize(40);
        assertThat(items2).hasSize(40);
        assertThat(items3).hasSize(20);

        for (int i = 0; i < 100; i++) {
            Item item = items.get(items.size() - 1 - i);
            if (i < 40) {
                assertThat(items1.get(i).getItemId()).isEqualTo(item.getItemId());
            } else if (i < 80) {
                assertThat(items2.get(i - 40).getItemId()).isEqualTo(item.getItemId());
            } else {
                assertThat(items3.get(i - 80).getItemId()).isEqualTo(item.getItemId());
            }
        }
    }
}