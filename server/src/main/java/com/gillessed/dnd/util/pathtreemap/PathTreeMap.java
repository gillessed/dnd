package com.gillessed.dnd.util.pathtreemap;

import java.util.Collection;
import java.util.function.BiConsumer;

public interface PathTreeMap<T, K extends PathTreeMap.Key<T, K>, V> {
    void put(K key, V value);
    PathTreeMapEntry<T, K, V> getEntry(K key);
    V get(K key);
    V remove(K key);
    void clear();
    void removeBranch(K key);
    void forEach(BiConsumer<K, PathTreeMapEntry<T, K, V>> consumer);
    Collection<K> keySet();
    Collection<V> valueSet();
    Collection<PathTreeMapEntry<T, K, V>> entrySet();

    interface Key<T, K extends Key<T, K>> {
        K getParent();
        K getCopy();
        K getChild(T value);
        T getValue();
    }
}
