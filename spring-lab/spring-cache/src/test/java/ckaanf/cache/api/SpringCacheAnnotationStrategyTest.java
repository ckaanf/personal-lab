package ckaanf.cache.api;

import ckaanf.cache.common.cache.CacheStrategy;
import ckaanf.cache.model.ItemCreateRequest;
import ckaanf.cache.model.ItemUpdateRequest;
import ckaanf.cache.service.response.ItemPageResponse;
import ckaanf.cache.service.response.ItemResponse;
import org.junit.jupiter.api.Test;

public class SpringCacheAnnotationStrategyTest {
    static final CacheStrategy CACHE_STRATEGY = CacheStrategy.SPRING_CACHE_ANNOTATION;

    @Test
    void createAndReadAndReadAllAndUpdateAndDelete() {
        ItemResponse item1 = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data1"));
        ItemResponse item2 = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data2"));
        ItemResponse item3 = ItemApiTestUtils.create(CACHE_STRATEGY, new ItemCreateRequest("data3"));

        //read
        ItemResponse item1Read1 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        ItemResponse item1Read2 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        ItemResponse item1Read3 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
        System.out.println("item1Read1 = " + item1Read1);
        System.out.println("item1Read2 = " + item1Read2);
        System.out.println("item1Read3 = " + item1Read3);


        ItemResponse item2Read1 = ItemApiTestUtils.read(CACHE_STRATEGY, item2.itemId());
        ItemResponse item2Read2 = ItemApiTestUtils.read(CACHE_STRATEGY, item2.itemId());
        ItemResponse item2Read3 = ItemApiTestUtils.read(CACHE_STRATEGY, item2.itemId());
        System.out.println("item2Read1 = " + item2Read1);
        System.out.println("item2Read2 = " + item2Read2);
        System.out.println("item2Read3 = " + item2Read3);


        ItemResponse item3Read1 = ItemApiTestUtils.read(CACHE_STRATEGY, item3.itemId());
        ItemResponse item3Read2 = ItemApiTestUtils.read(CACHE_STRATEGY, item3.itemId());
        ItemResponse item3Read3 = ItemApiTestUtils.read(CACHE_STRATEGY, item3.itemId());
        System.out.println("item3Read1 = " + item3Read1);
        System.out.println("item3Read2 = " + item3Read2);
        System.out.println("item3Read3 = " + item3Read3);

        //readAll
        ItemPageResponse itemPageReadAll1 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1L, 2L);
        ItemPageResponse itemPageReadAll2 = ItemApiTestUtils.readAll(CACHE_STRATEGY, 1L, 2L);
        System.out.println("itemPageReadAll1 = " + itemPageReadAll1);
        System.out.println("itemPageReadAll2 = " + itemPageReadAll2);

        //readAllIS
        ItemPageResponse itemPageReadAllIS1 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 2L);
        ItemPageResponse itemPageReadAllIS2 = ItemApiTestUtils.readAllInfiniteScroll(CACHE_STRATEGY, null, 2L);
        System.out.println("itemPageReadAllIS1 = " + itemPageReadAllIS1);
        System.out.println("itemPageReadAllIS2 = " + itemPageReadAllIS2);

        //update
        ItemResponse updatedData = ItemApiTestUtils.update(CACHE_STRATEGY, item1.itemId(), new ItemUpdateRequest("update"));
        ItemResponse updatedDataRead1 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());

        System.out.println("updatedData = " + updatedDataRead1);

        //delete
        ItemApiTestUtils.delete(CACHE_STRATEGY, item1.itemId());
        try {
            ItemResponse deletedDataRead1 = ItemApiTestUtils.read(CACHE_STRATEGY, item1.itemId());
            System.out.println("deletedDataRead1 = " + deletedDataRead1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Item not found after deletion");
        }


    }
}
