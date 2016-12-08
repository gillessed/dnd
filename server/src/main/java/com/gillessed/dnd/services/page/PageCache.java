package com.gillessed.dnd.services.page;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.WikiPage;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Singleton
public class PageCache {
    private final Set<PageCacheEntry> pageCacheEntries;
    private final ReentrantReadWriteLock lock;
    private final long timeToLive;

    @Inject
    public PageCache(DndConfiguration configuration) {
        pageCacheEntries = new HashSet<>();
        lock = new ReentrantReadWriteLock();
        this.timeToLive = configuration.getPageCacheTimeToLive();
        startReaper();
    }

    private void startReaper() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            lock.readLock().lock();
            List<PageCacheEntry> pageCacheEntriesToRemove = new ArrayList<>();
            try {
                for (PageCacheEntry entry : pageCacheEntries) {
                    if (System.currentTimeMillis() - entry.getTimestamp() > timeToLive) {
                        pageCacheEntriesToRemove.add(entry);
                    }
                }
            } finally {
                lock.readLock().unlock();
            }
            lock.writeLock().lock();
            try {
                this.pageCacheEntries.removeAll(pageCacheEntriesToRemove);
            } finally {
                lock.writeLock().unlock();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    public Optional<PageCacheEntry> getPageByPath(@NotNull Path path) {
        Preconditions.checkNotNull(path);
        lock.readLock().lock();
        PageCacheEntry matchingEntry = null;
        try {
            for (PageCacheEntry entry : pageCacheEntries) {
                if (path.equals(entry.getPath())) {
                    matchingEntry = entry;
                    break;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return Optional.ofNullable(matchingEntry);
    }

    public List<PageCacheEntry> getPagesByFilename(@NotNull String filename) {
        Preconditions.checkNotNull(filename);
        lock.readLock().lock();
        List<PageCacheEntry> matchingEntries = new ArrayList<>();
        try {
            for (PageCacheEntry entry : pageCacheEntries) {
                if (filename.equals(entry.getPath().getFileName().toString())) {
                    matchingEntries.add(entry);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return matchingEntries;
    }

    public void addToCache(@NotNull WikiPage page, @NotNull Path pagePath) {
        Preconditions.checkNotNull(page);
        Preconditions.checkNotNull(pagePath);
        lock.writeLock().lock();
        try {
            Set<PageCacheEntry> filter = pageCacheEntries.stream()
                    .filter((PageCacheEntry entry) -> !entry.getPath().equals(pagePath))
                    .collect(Collectors.toSet());
            pageCacheEntries.clear();
            pageCacheEntries.addAll(filter);
            pageCacheEntries.add(new PageCacheEntry.Builder()
                    .page(page)
                    .path(pagePath)
                    .timestamp(System.currentTimeMillis())
                    .build());
        } finally {
            lock.writeLock().unlock();
        }
    }
}
