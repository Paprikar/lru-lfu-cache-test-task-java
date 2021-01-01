package dev.paprikar.caching.lru;

import dev.paprikar.caching.ICache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> implements ICache<K, V> {

    final int capacity;
    final Map<K, CacheNode<K, V>> cache;

    void moveNodeToTail(CacheNode<K, V> cacheNode) {
        cache.remove(cacheNode.key);
        cache.put(cacheNode.key, cacheNode);
    }

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        this.capacity = capacity;
        cache = new LinkedHashMap<>();
    }

    public V get(K key) {
        CacheNode<K, V> cacheNode;
        if ((cacheNode = cache.get(key)) == null)
            return null;
        moveNodeToTail(cacheNode);
        return cacheNode.value;
    }

    public V put(K key, V value) {
        V oldValue = null;
        CacheNode<K, V> cacheNode;
        if ((cacheNode = cache.get(key)) == null) {
            if (cache.size() >= capacity) { // do eviction
                cache.remove(cache.keySet().iterator().next());
            }
            cacheNode = new CacheNode<>(key, value); // node to add
            cache.put(key, cacheNode);
        } else { // key is already added
            oldValue = cacheNode.value;
            cacheNode.value = value;
            moveNodeToTail(cacheNode);
        }
        return oldValue;
    }

    public V remove(K key) {
        CacheNode<K, V> cacheNode;
        return (cacheNode = cache.remove(key)) == null ? null : cacheNode.value;
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        return cache.size();
    }
}
