package com.gillessed.dnd.services.search.index;

import com.gillessed.dnd.rest.api.response.search.ImmutableSearchResult;
import com.gillessed.dnd.rest.api.response.search.SearchResult;
import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class MapIndex implements Index {
    private final ListMultimap<String, IndexEntry> entryMap;
    private final ReentrantReadWriteLock lock;

    public MapIndex() {
        entryMap = ArrayListMultimap.create();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public List<SearchResult> getResultsForQuery(@Nonnull String query) {
        query = query.toLowerCase();
        Preconditions.checkNotNull(query);
        lock.readLock().lock();
        try {
            List<IndexEntry> exactMatches = new ArrayList<>();
            List<IndexEntry> startWithMatches = new ArrayList<>();
            List<IndexEntry> partialMatches = new ArrayList<>();
            for (IndexEntry entry : entryMap.values()) {
                if (entry.getKey().equals(query)) {
                    exactMatches.add(entry);
                } else if (entry.getKey().startsWith(query)) {
                    startWithMatches.add(entry);
                } else if (entry.getKey().contains(query)) {
                    partialMatches.add(entry);
                }
            }

            List<SearchResult> results = new ArrayList<>();
            Comparator<IndexEntry> comparator = (IndexEntry i1, IndexEntry i2) -> {
                Integer i1Level = i1.getLevel().ordinal();
                Integer i2Level = i2.getLevel().ordinal();
                return i1Level.compareTo(i2Level);
            };
            exactMatches.sort(comparator);
            startWithMatches.sort(comparator);
            partialMatches.sort(comparator);

            Consumer<IndexEntry> entryConsumer = (IndexEntry entry) -> {
                SearchResult result = ImmutableSearchResult.builder()
                        .title(entry.getTitle())
                        .description(entry.getDescription())
                        .target(entry.getTarget())
                        .build();
                results.add(result);
            };
            exactMatches.forEach(entryConsumer);
            startWithMatches.forEach(entryConsumer);
            partialMatches.forEach(entryConsumer);
            return results;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void updateSource(@Nonnull String source, @Nonnull List<IndexEntry> entries) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(entries);
        lock.writeLock().lock();
        entryMap.removeAll(source);
        entryMap.putAll(source, entries);
        lock.writeLock().unlock();
    }

    @Override
    public void deleteSource(@Nonnull String source) {
        Preconditions.checkNotNull(source);
        entryMap.removeAll(source);
    }

    @Override
    public String toString() {
        lock.readLock().lock();
        try {
            return entryMap.toString();
        } finally {
            lock.readLock().unlock();
        }
    }
}
