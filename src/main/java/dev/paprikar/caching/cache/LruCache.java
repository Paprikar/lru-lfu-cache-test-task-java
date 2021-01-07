package dev.paprikar.caching.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This implementation uses {@code LinkedHashMap} to store information
 * about the keys and their corresponding values in order of addition.
 * This provides information about the least recently used key at the beginning
 * of the list of the map and the possibility of deleting it in case of a cache overflow.
 * Moving the key to the end of the map list is done by deleting and then adding it.
 * The key is moved to the end of the list of the map in the
 * case of his addition or getting, including modifying the value.
 *
 * <p>
 * All this allows to perform add / get / remove operations in a constant {@code O(1)} time.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 * @author paprikar
 */
public class LruCache<K, V> implements ICache<K, V> {

    /* ---------------------------------------------------------------- */
    // Fields

    /**
     * The capacity factor used when none specified in constructor.
     */
    static final float DEFAULT_CAPACITY_FACTOR = 1.5f;

    /**
     * The cache capacity.
     */
    final int capacity;

    /**
     * Map for mapping keys and their values.
     */
    final Map<K, V> cache;

    /* ---------------------------------------------------------------- */
    // Public operations

    /**
     * Constructs a {@link LruCache} with the specified capacity and capacity factor.
     * {@code HashMap} like objects will be created like:<pre>
     * new HashMap<>((int) (capacity * capacityFactor), 1.0f);</pre>
     *
     * @param capacity       the cache capacity.
     * @param capacityFactor the capacity factor that affects the capacity of {@code HashMap} like objects.
     * @throws IllegalArgumentException if the capacity or capacity factor is less than one.
     */
    public LruCache(int capacity, float capacityFactor) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
        if (capacityFactor < 1.0f) {
            throw new IllegalArgumentException("Illegal capacity factor: " + capacityFactor);
        }
        this.capacity = capacity;
        int hashMapCapacity = (int) (capacity * capacityFactor);
        cache = new LinkedHashMap<>(hashMapCapacity, 1.0f);
    }

    /**
     * Constructs a {@link LruCache} with the specified capacity and the default capacity factor (1.5).
     *
     * @param capacity the cache capacity.
     * @throws IllegalArgumentException if the capacity is less than one.
     */
    public LruCache(int capacity) {
        this(capacity, DEFAULT_CAPACITY_FACTOR);
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
        V value = cache.remove(key);
        if (value == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        K k = (K) key;
        cache.put(k, value);
        return value;
    }

    public V put(K key, V value) {
        V oldValue;
        V v;
        if ((v = cache.get(key)) == null) {
            oldValue = null;
            if (cache.size() >= capacity) {
                // do eviction
                K k = cache.keySet().iterator().next();
                cache.remove(k);
            }
        } else {
            // key is already added
            oldValue = v;
            cache.remove(key);
        }
        cache.put(key, value);
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
