package ckaanf.cache.repository;

import ckaanf.cache.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Data Source
 * 데이터베이스 OR 외부 API OR 무거운 연산
 * 실제 DB 연동을 대신해서 활용
 */
@Slf4j
@Repository
public class ItemRepository {
    private final ConcurrentSkipListMap<Long, Item> database = new ConcurrentSkipListMap<>(Comparator.reverseOrder());

    public Optional<Item> read(Long itemId) {
        log.info("[ItemRepository.read] itemId = {} ", itemId);
        return Optional.ofNullable(database.get(itemId));
    }

    public List<Item> readAll(Long page, Long pageSize) {
        log.info("[ItemRepository.readAll] page = {} pageSize = {} ", page, pageSize);
        return database.values().stream().skip((page - 1) * pageSize).limit(pageSize).toList();
    }

    public List<Item> readAllInfiniteScroll(Long lastItemId, Long pageSize) {
        log.info("[ItemRepository.readAllInfiniteScroll] lastItemId = {} pageSize = {} ", lastItemId, pageSize);
        if (lastItemId == null) {
            return database.values().stream().limit(pageSize).toList();
        }
        return database.tailMap(lastItemId, false).values().stream().limit(pageSize).toList();
    }

    public Item create(Item item) {
        log.info("[ItemRepository.create] item = {} ", item);
        database.put(item.getItemId(), item);
        return item;
    }

    public Item update(Item item) {
        log.info("[ItemRepository.update] item = {} ", item);
        database.put(item.getItemId(), item);
        return item;
    }

    public void delete(Long itemId) {
        log.info("[ItemRepository.delete] itemId = {} ", itemId);
        database.remove(itemId);
    }

    public long count() {
        log.info("[ItemRepository.count] ");
        return database.size();
    }
}
