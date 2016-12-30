package com.gillessed.dnd.util.filewatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RecursiveFileWatcher implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RecursiveFileWatcher.class);

    private final Path rootFolder;
    private final Set<RecursiveFileWatcher.Listener> listeners;
    private final RecursiveFileWatcher.WatchRegister watchRegister;
    private boolean running;

    public RecursiveFileWatcher(Path rootFolder) {
        this.rootFolder = rootFolder;
        this.listeners = new HashSet<>();
        this.watchRegister = new RecursiveFileWatcher.WatchRegister();
        this.running = false;
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            watchRegister.watchService = watchService;
            watchRegister.registerRecursive(rootFolder);
            while (running) {
                try {
                    WatchKey watchKey = watchService.take();
                    Path dir = watchRegister.getPathForKey(watchKey);
                    for (WatchEvent<?> event : watchKey.pollEvents()) {
                        handleWatchEvent(dir, event);
                    }
                    if (!watchKey.reset()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    log.error("File watcher was interrupted.", e);
                }
            }
        } catch (IOException e) {
            log.error("There was an critical error with the file watcher.", e);
        }
    }

    public void stop() {
        running = false;
    }

    private void handleWatchEvent(Path dir, WatchEvent<?> event) throws IOException {
        WatchEvent.Kind<?> kind = event.kind();
        if (kind == StandardWatchEventKinds.OVERFLOW) {
            log.warn("Watch event overflow.");
            return;
        }

        WatchEvent<Path> ev = (WatchEvent<Path>) event;
        Path pageFile = dir.resolve(ev.context());
        if (Files.isDirectory(pageFile)) {
            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                watchRegister.register(pageFile);
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                watchRegister.unregister(pageFile);
            }
        } else {
            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                for (RecursiveFileWatcher.Listener listener : listeners) {
                    listener.onFileAdded(pageFile);
                }
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                for (RecursiveFileWatcher.Listener listener : listeners) {
                    listener.onFileModified(pageFile);
                }
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                for (RecursiveFileWatcher.Listener listener : listeners) {
                    listener.onFileRemoved(pageFile);
                }
            }
        }
    }

    public interface Listener {
        void onFileAdded(Path path);
        void onFileModified(Path path);
        void onFileRemoved(Path path);
    }

    public void addListener(RecursiveFileWatcher.Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(RecursiveFileWatcher.Listener listener) {
        listeners.remove(listener);
    }

    private class WatchRegister {
        private final Map<WatchKey, Path> watchKeys;
        private WatchService watchService;

        private WatchRegister() {
            this.watchKeys = new HashMap<>();
        }

        private void register(Path dir) throws IOException {
            WatchKey key = dir.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            watchKeys.put(key, dir.toAbsolutePath());
        }

        private void unregister(Path dir) throws IOException {
            Files.walkFileTree(rootFolder, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    WatchKey key = dir.register(watchService);
                    watchKeys.remove(key);
                    key.cancel();
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        private void registerRecursive(Path dir) throws IOException {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    register(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        private Path getPathForKey(WatchKey watchKey) {
            return watchKeys.get(watchKey);
        }
    }
}
