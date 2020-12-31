package dev.paprikar.caching;

import dev.paprikar.caching.lfu.LfuCache;
import dev.paprikar.caching.lru.LruCache;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CachingApplicationTests {

    @Test
    void testCacheExceptions() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cache<>(10, CacheDeletionStrategy.SOME_NEW_STRATEGY)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new LruCache<>(0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new LruCache<>(-10)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new LfuCache<>(0)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new LfuCache<>(-10)
        );
    }

    @Test
    void testCacheLru() {
        Cache<Integer, Integer> cache = new Cache<>(2, CacheDeletionStrategy.LRU);
        assertNull(cache.put(1, 1));
        assertNull(cache.put(2, 2));
        assertEquals(1, cache.get(1));
        assertNull(cache.put(3, 3));
        assertNull(cache.get(2));
        assertEquals(3, cache.put(3, 4));
        assertNull(cache.put(4, 5));
        assertNull(cache.get(1));
        assertEquals(4, cache.get(3));
        assertEquals(5, cache.get(4));
    }

    @Test
    void testCacheLfu() {
        Cache<Integer, Integer> cache = new Cache<>(2, CacheDeletionStrategy.LFU);
        assertNull(cache.put(1, 1));
        assertNull(cache.put(2, 2));
        assertEquals(1, cache.get(1));
        assertEquals(1, cache.get(1));
        assertNull(cache.put(3, 3));
        assertNull(cache.get(2));
        assertEquals(3, cache.put(3, 4));
        assertNull(cache.put(4, 5));
        assertEquals(1, cache.get(1));
        assertNull(cache.get(3));
        assertEquals(5, cache.get(4));
    }
}
