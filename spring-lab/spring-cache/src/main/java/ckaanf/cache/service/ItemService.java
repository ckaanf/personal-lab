package ckaanf.cache.service;

import ckaanf.cache.model.Item;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.repository.ItemRepository;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemResponse read(Long itemId) {
        return itemRepository.read(itemId).map(ItemResponse::from).orElse(null);
    }

    public ItemPageResponse readAll(Long page, Long size) {
        return ItemPageResponse.from(
                itemRepository.readAll(page, size),
                itemRepository.count()
        );
    }

    public ItemPageResponse readAllInfiniteScroll(Long lastItemId, Long size) {
        return ItemPageResponse.from(
                itemRepository.readAllInfiniteScroll(lastItemId, size),
                itemRepository.count()
        );
    }

    public ItemResponse create(ItemCreateRequest request) {
        return ItemResponse.from(
                itemRepository.create(Item.create(request))
        );
    }

    public ItemResponse update(Long itemId, ItemUpdateRequest request) {
        Item item = itemRepository.read(itemId).orElseThrow();
        item.update(request);
        return ItemResponse.from(
                itemRepository.update(item)
        );
    }

    public void delete(Long itemId) {
        itemRepository.read(itemId).ifPresent(item -> itemRepository.delete(item.getItemId()));
    }

    public long count() {
        return itemRepository.count();
    }
}
