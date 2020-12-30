package dev.paprikar.caching.lru;

public class CacheNode<K, V> {

    final K key;
    V value;

    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
