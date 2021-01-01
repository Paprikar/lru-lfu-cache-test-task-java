package dev.paprikar.caching;

public interface ICache<K, V> {

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    void clear();

    int size();
}
