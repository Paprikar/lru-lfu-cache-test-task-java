package dev.paprikar.caching.lru;

import dev.paprikar.caching.ICache;

import java.util.HashMap;
import java.util.Map;

public class LruCache<K, V> implements ICache<K, V> {

    final int capacity;
    final Map<K, Node<K, V>> cache;
    final Node<K, V> head, tail;

    public LruCache(int capacity) {
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
    }

    private void addNode(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node<K, V> node) {
        Node<K, V> prev = node.prev;
        Node<K, V> next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    private void moveNodeToHead(Node<K, V> node) {
        removeNode(node);
        addNode(node);
    }

    public V get(K key) {
        Node<K, V> node;
        if ((node = cache.get(key)) == null)
            return null;
        moveNodeToHead(node);
        return node.getValue();
    }

    public V put(K key, V value) {
        Node<K, V> node;
        if ((node = cache.get(key)) != null) { // key is already added
            V oldValue = node.value;
            node.value = value;
            moveNodeToHead(node);
            return oldValue;
        }
        node = new Node<>(key, value); // node to add
        cache.put(key, node);
        addNode(node);
        if (cache.size() > capacity) {
            node = tail.prev; // last node
            removeNode(node);
            cache.remove(node.key);
        }
        return null;
    }
}
