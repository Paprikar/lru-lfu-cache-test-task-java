package dev.paprikar.caching;

import dev.paprikar.caching.lfu.LfuCache;
import dev.paprikar.caching.lru.LruCache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Cache<K, V> implements ICache<K, V> {

    final ICache<K, V> cache;

    public Cache(int capacity, CacheDeletionStrategy strategy) {
        switch (strategy) {
            case LFU:
                cache = new LfuCache<>(capacity);
                break;
            case LRU:
                cache = new LruCache<>(capacity);
                break;
            default:
                throw new IllegalArgumentException("Illegal strategy: " + strategy);
        }
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    public V get(Object key) {
        return cache.get(key);
    }

    public V put(K key, V value) {
        return cache.put(key, value);
    }

    public V remove(Object key) {
        return cache.remove(key);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        cache.putAll(m);
    }

    public void clear() {
        cache.clear();
    }

    public Set<K> keySet() {
        return cache.keySet();
    }

    public Collection<V> values() {
        return cache.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return cache.entrySet();
    }
}
