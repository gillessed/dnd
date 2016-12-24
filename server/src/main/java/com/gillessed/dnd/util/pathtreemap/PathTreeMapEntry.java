package com.gillessed.dnd.util.pathtreemap;

import java.util.Set;

public interface PathTreeMapEntry<T, K extends PathTreeMap.Key<T, K>, V> {
    void set(V value);
    V get();
    K getKey();
    void addChild(PathTreeMapEntry<T, K, V> value);
    void removeChild(PathTreeMapEntry<T, K, V> value);
    Set<PathTreeMapEntry<T, K, V>> getChildren();
}
