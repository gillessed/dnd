package com.gillessed.dnd.util.pathtreemap.impl;

import com.gillessed.dnd.util.pathtreemap.PathTreeMap;
import com.gillessed.dnd.util.pathtreemap.PathTreeMapEntry;

import java.util.HashSet;
import java.util.Set;

public class PathTreeMapSimpleEntry<T, K extends PathTreeMap.Key<T, K>, V> implements PathTreeMapEntry<T, K, V> {
    private final K key;
    private V value;
    private final Set<PathTreeMapEntry<T, K, V>> children;

    public PathTreeMapSimpleEntry(K key) {
        this.children = new HashSet<>();
        this.key = key;
    }

    @Override
    public void set(V value) {
        this.value = value;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public void addChild(PathTreeMapEntry<T, K, V> value) {
        children.add(value);
    }

    @Override
    public void removeChild(PathTreeMapEntry<T, K, V> value) {
        children.remove(value);
    }

    @Override
    public Set<PathTreeMapEntry<T, K, V>> getChildren() {
        return children;
    }
}
