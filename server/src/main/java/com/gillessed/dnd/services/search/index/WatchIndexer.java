package com.gillessed.dnd.services.search.index;

import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiTitle;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.services.page.PageService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Watches for filesystem changes and reindexes the files
 * upon change.
 *
 * It's not super performant but it's great for testing.
 */
public class WatchIndexer implements Indexer, Runnable {
    private final Logger log = LoggerFactory.getLogger(WatchIndexer.class);

    private final PageService pageService;
    private final Path rootFolder;
    private final Index index;
    private final ExecutorService executorService;
    private final WatchRegister watchRegister;
    private boolean running;

    public WatchIndexer(PageService pageService, String root) {
        this.pageService = pageService;
        this.rootFolder = Paths.get(root).toAbsolutePath();
        Preconditions.checkState(Files.exists(rootFolder));
        Preconditions.checkState(Files.isDirectory(rootFolder));
        this.index = new MapIndex();
        this.executorService = Executors.newSingleThreadExecutor();
        this.watchRegister = new WatchRegister();
        this.running = true;
    }

    @Override
    public void start() {
        log.info("Watch indexer watching directory {}", rootFolder);
        executorService.submit(this);
    }

    @Override
    public void stop() {
        log.info("Stopping watch indexer on directory {}", rootFolder);
        running = false;
    }

    @Override
    public Index getIndex() {
        return index;
    }

    @Override
    public void run() {
        FileSystem fileSystem = rootFolder.getFileSystem();
        try (WatchService watchService = fileSystem.newWatchService()) {
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
                    log.error("Watch indexer was interrupted.", e);
                }
            }
        } catch (IOException e) {
            log.error("There was an error watching for index changes.", e);
        }
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
            watchRegister.register(pageFile);
        } else {
            log.info("Indexing file {}", pageFile);
            indexPage(pageFile, kind == StandardWatchEventKinds.ENTRY_DELETE);
        }
    }

    @Override
    public synchronized void indexPage(Path pageFileIn, boolean deleted) throws IOException {
        Path pageFile = pageFileIn.normalize().toAbsolutePath();
        if (pageFile.getFileName().toString().equals("index")) {
            pageFile = pageFile.getParent();
        }
        String source = rootFolder.relativize(pageFile).toString();

        if (!deleted) {
            WikiPage page;
            try {
                page = pageService.getPage(pageFile, false, false);
            } catch (ParsingException e) {
                log.error("Could not parse page {}. Skipping index...", e);
                return;
            }
            String target = source.replace(File.separator, "_");
            List<IndexEntry> entries = new ArrayList<>();
            WikiTitle title = page.getTitleObject();
            IndexEntry entry = ImmutableIndexEntry.builder()
                    .source(source)
                    .target(target)
                    .title(page.getTitle())
                    .description(page.getDescription())
                    .key(title.getText().toLowerCase())
                    .level(IndexEntry.Level.TITLE)
                    .build();
            entries.add(entry);
            index.updateSource(source, entries);
        } else {
            index.deleteSource(source);
        }
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

        private void registerRecursive(Path dir) throws IOException {
            Files.walkFileTree(rootFolder, new SimpleFileVisitor<Path>() {
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
