package dev.paprikar.caching;

public interface ICache<K, V> {

    V get(K key);

    V put(K key, V value);
}
