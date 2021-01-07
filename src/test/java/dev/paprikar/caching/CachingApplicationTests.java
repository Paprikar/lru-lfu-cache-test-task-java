package dev.paprikar.caching;

import dev.paprikar.caching.cache.Cache;
import dev.paprikar.caching.cache.CacheDeletionStrategy;
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
                new Cache<>(0, CacheDeletionStrategy.LRU)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Cache<>(-10, CacheDeletionStrategy.LRU)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Cache<>(0, CacheDeletionStrategy.LFU)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Cache<>(-10, CacheDeletionStrategy.LFU)
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
        assertEquals(5, cache.remove(4));
    }

    @Test
    void testCacheLfu() {
        Cache<Integer, Integer> cache = new Cache<>(2, CacheDeletionStrategy.LFU);
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
        assertEquals(4, cache.remove(3));
        assertEquals(5, cache.remove(4));
    }
}
