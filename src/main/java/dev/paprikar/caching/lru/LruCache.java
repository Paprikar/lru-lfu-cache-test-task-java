package dev.paprikar.caching.lru;

import dev.paprikar.caching.ICache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LruCache<K, V> implements ICache<K, V> {

    /* ---------------------------------------------------------------- */
    // Fields

    final int capacity;
    final Map<K, V> cache;

    /* ---------------------------------------------------------------- */
    // Internal utilities

    void moveKeyToTail(K key) {
        V value = cache.remove(key);
        cache.put(key, value);
    }

    /* ---------------------------------------------------------------- */
    // Public operations

    public LruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        this.capacity = capacity;
        cache = new LinkedHashMap<>();
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
        V value;
        if ((value = cache.get(key)) == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        K k = (K) key;
        moveKeyToTail(k);
        return value;
    }

    public V put(K key, V value) {
        V oldValue = null;
        V v;
        if ((v = cache.get(key)) == null) {
            if (cache.size() >= capacity) {
                // do eviction
                K k = cache.keySet().iterator().next();
                cache.remove(k);
            }
            cache.put(key, value);
        } else {
            // key is already added
            oldValue = v;
            cache.put(key, value);
            moveKeyToTail(key);
        }
        return oldValue;
    }

    public V remove(Object key) {
        V value;
        return (value = cache.remove(key)) == null ? null : value;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) {
            return;
        }
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            K k = e.getKey();
            V v = e.getValue();
            put(k, v);
        }
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
