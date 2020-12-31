package dev.paprikar.caching.lfu;

public class CacheNode<K, V> {

    final K key;
    V value;
    long frequency;

    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public CacheNode(K key, V value, long frequency) {
        this.key = key;
        this.value = value;
        this.frequency = frequency;
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

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
}
