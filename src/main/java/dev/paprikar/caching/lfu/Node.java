package dev.paprikar.caching.lfu;

import java.util.Map;
import java.util.Objects;

public class Node<K, V> implements Map.Entry<K, V> {

    Node<K, V> prev;
    Node<K, V> next;
    final K key;
    V value;
    long frequency;

    public Node(K key, V value, long frequency) {
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

    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    public void insertPrevious(Node<K, V> node) {
        node.prev = prev;
        node.next = this;
        if (prev != null) {
            prev.next = node;
        }
        prev = node;
    }

    public void insertNext(Node<K, V> node) {
        node.next = next;
        node.prev = this;
        if (next != null) {
            next.prev = node;
        }
        next = node;
    }

    public void unlink() {
        Node<K, V> p = prev;
        Node<K, V> n = next;
        if (p != null) {
            p.next = n;
            prev = null;
        }
        if (n != null) {
            n.prev = p;
            next = null;
        }
    }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            return Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue());
        }
        return false;
    }
}
