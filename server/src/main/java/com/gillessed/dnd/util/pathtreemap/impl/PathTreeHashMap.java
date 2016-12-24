package com.gillessed.dnd.util.pathtreemap.impl;

import com.gillessed.dnd.util.pathtreemap.PathTreeMap;
import com.gillessed.dnd.util.pathtreemap.PathTreeMapEntry;
import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Direct references to entrees are stored in a hash map.
 *
 * This implementation is thread safe.
 */
public class PathTreeHashMap<T, K extends PathTreeMap.Key<T, K>, V>  implements PathTreeMap<T, K, V>  {
    private final Map<K, PathTreeMapEntry<T, K, V>> entryMap;

    public PathTreeHashMap() {
        this.entryMap = new HashMap<>();
    }

    @Override
    public void put(K key, V value) {
        Preconditions.checkNotNull(key, "Cannot put empty key into path tree map.");
        K iterator = key;
        LinkedList<K> parents = new LinkedList<K>();
        while (iterator != null) {
            parents.addFirst(iterator);
            iterator = iterator.getParent();
        }
        parents.forEach((K parentKey) -> {
            if (!entryMap.containsKey(parentKey)) {
                insertEmptyNodeAt(parentKey);
            }
        });
        entryMap.get(key).set(value);
    }

    private void insertEmptyNodeAt(K key) {
        PathTreeMapEntry<T, K, V> entry = new PathTreeMapSimpleEntry<>(key);
        entryMap.put(key, entry);
        K parentKey = key.getParent();
        if (parentKey != null) {
            entryMap.get(parentKey).addChild(entry);
        }
    }

    @Override
    public PathTreeMapEntry<T, K, V> getEntry(K key) {
        Preconditions.checkNotNull(key, "Cannot get entry with empty key from path tree map.");
        return entryMap.getOrDefault(key, null);
    }

    @Override
    public V get(K key) {
        Preconditions.checkNotNull(key, "Cannot get with empty key from path tree map.");
        PathTreeMapEntry<T, K, V> entry = entryMap.getOrDefault(key, null);
        if (entry == null) {
            return null;
        }
        return entry.get();
    }

    @Override
    public V remove(K key) {
        Preconditions.checkNotNull(key, "Cannot remove with empty key from path tree map.");
        PathTreeMapEntry<T, K, V> entry = entryMap.getOrDefault(key, null);
        if (entry == null) {
            return null;
        }
        V value = entry.get();
        entry.set(null);
        return value;
    }

    @Override
    public void removeBranch(K key) {
        Preconditions.checkNotNull(key, "Cannot remove branch with empty key from path tree map.");
        PathTreeMapEntry<T, K, V> rootEntry = entryMap.getOrDefault(key, null);
        if (rootEntry == null) {
            return;
        }
        Stack<PathTreeMapEntry<T, K, V>> entryStack = new Stack<>();
        entryStack.push(rootEntry);
        while (!entryStack.isEmpty()) {
            PathTreeMapEntry<T, K, V> entry = entryStack.pop();
            entryMap.remove(entry);
            entry.set(null);
            entry.getChildren().forEach(entryStack::push);
        }
    }

    @Override
    public void forEach(BiConsumer<K, PathTreeMapEntry<T, K, V>> consumer) {
        entryMap.forEach(consumer);
    }

    @Override
    public Collection<K> keySet() {
        return entryMap.keySet();
    }

    @Override
    public Collection<V> valueSet() {
        return entryMap.values().stream()
                .map(PathTreeMapEntry::get)
                .filter((V value) -> value != null)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<PathTreeMapEntry<T, K, V>> entrySet() {
        return entryMap.values();
    }

    @Override
    public String toString() {
        return "PathTreeHashMap{" +
                "entryMap=" + entryMap +
                '}';
    }
}
