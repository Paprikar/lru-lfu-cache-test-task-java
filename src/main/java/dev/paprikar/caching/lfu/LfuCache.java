package dev.paprikar.caching.lfu;

import dev.paprikar.caching.ICache;

import java.util.*;

public class LfuCache<K, V> implements ICache<K, V> {

    final int capacity;
    final Map<K, CacheNode<K, V>> cache;
    final Map<Long, Set<K>> lrus;
    long minFrequency;

    void addKeyToFrequency(K key, long frequency) {
        Set<K> keys;
        if ((keys = lrus.get(frequency)) == null) {
            keys = new LinkedHashSet<>();
            lrus.put(frequency, keys);
        }
        keys.add(key);
    }

    void incrementNodeFrequency(CacheNode<K, V> cacheNode) {
        long frequency = cacheNode.frequency;
        Set<K> keys = lrus.get(frequency);
        keys.remove(cacheNode.key);
        if (keys.isEmpty()) {
            lrus.remove(frequency);
            if (frequency == minFrequency) {
                minFrequency++;
            }
        }
        long newFrequency = frequency + 1;
        cacheNode.frequency = newFrequency;
        addKeyToFrequency(cacheNode.key, newFrequency);
    }

    public LfuCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        this.capacity = capacity;
        cache = new HashMap<>();
        lrus = new HashMap<>();
    }

    public V get(K key) {
        CacheNode<K, V> cacheNode;
        if ((cacheNode = cache.get(key)) == null) {
            return null;
        }
        incrementNodeFrequency(cacheNode);
        return cacheNode.value;
    }

    public V put(K key, V value) {
        V oldValue = null;
        CacheNode<K, V> cacheNode;
        if ((cacheNode = cache.get(key)) == null) {
            if (cache.size() >= capacity) { // do eviction
                Set<K> keys = lrus.get(minFrequency);
                K k = keys.iterator().next();
                cache.remove(k);
                keys.remove(k);
                if (keys.isEmpty()) {
                    lrus.remove(minFrequency);
                }
            }
            minFrequency = 0L;
            cacheNode = new CacheNode<>(key, value); // node to add
            addKeyToFrequency(key, 0L);
            cache.put(key, cacheNode);
        } else { // key is already added
            oldValue = cacheNode.value;
            cacheNode.value = value;
            incrementNodeFrequency(cacheNode);
        }
        return oldValue;
    }

    public V remove(K key) {
        CacheNode<K, V> cacheNode = cache.remove(key);
        if (cacheNode != null) {
            long frequency = cacheNode.frequency;
            Set<K> keys = lrus.get(frequency);
            keys.remove(key);
            if (keys.isEmpty()) {
                lrus.remove(frequency);
                if (frequency == minFrequency && !lrus.isEmpty()) {
                    minFrequency = Collections.min(lrus.keySet());
                }
            }
            return cacheNode.value;
        }
        return null;
    }

    public void clear() {
        cache.clear();
        lrus.clear();
    }

    public int size() {
        return cache.size();
    }
}
