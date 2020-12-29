package dev.paprikar.caching;

import dev.paprikar.caching.lfu.LfuCache;
import dev.paprikar.caching.lru.LruCache;

public class Cache<K, V> implements ICache<K, V> {

    ICache<K, V> cache;

    public Cache(int capacity, CacheDeletionStrategy strategy) {
        switch (strategy) {
            case LFU:
                cache = new LfuCache<>(capacity);
                break;
            case LRU:
                cache = new LruCache<>(capacity);
                break;
        }
    }

    public V get(K key) {
        return cache.get(key);
    }

    public V put(K key, V value) {
        return cache.put(key, value);
    }
}
