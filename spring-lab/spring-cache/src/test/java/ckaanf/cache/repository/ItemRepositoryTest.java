package ckaanf.cache.repository;

import ckaanf.cache.model.Item;
import ckaanf.cache.model.ItemCreateRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @Test
    void readAll() {
        // given
        List<Item> list = IntStream.range(0, 3).mapToObj(i -> itemRepository.create(Item.create(new ItemCreateRequest("data" + i)))).toList();

        // when
        List<Item> first = itemRepository.readAll(1L, 2L);
        List<Item> second = itemRepository.readAll(2L, 2L);

        assertThat(first).hasSize(2);
        assertThat(first.get(0).getItemId()).isEqualTo(list.get(2).getItemId());
        assertThat(first.get(1).getItemId()).isEqualTo(list.get(1).getItemId());

        assertThat(second).hasSize(1);
        assertThat(second.get(0).getItemId()).isEqualTo(list.get(0).getItemId());
    }

    @Test
    void readAllInfiniteScroll() {
        // given
        List<Item> list = IntStream.range(0, 3).mapToObj(i -> itemRepository.create(Item.create(new ItemCreateRequest("data" + i)))).toList();

        // when
        List<Item> first = itemRepository.readAllInfiniteScroll(null, 2L);
        List<Item> second = itemRepository.readAllInfiniteScroll(first.getLast().getItemId(), 2L);

        assertThat(first).hasSize(2);
        assertThat(first.get(0).getItemId()).isEqualTo(list.get(2).getItemId());
        assertThat(first.get(1).getItemId()).isEqualTo(list.get(1).getItemId());

        assertThat(second).hasSize(1);
        assertThat(second.get(0).getItemId()).isEqualTo(list.get(0).getItemId());

    }
}