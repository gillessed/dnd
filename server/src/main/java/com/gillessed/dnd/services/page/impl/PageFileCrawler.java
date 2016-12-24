package com.gillessed.dnd.services.page.impl;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.page.PageTransformer;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.services.page.PageProvider;
import com.gillessed.dnd.util.filewatcher.RecursiveFileWatcher;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
public class PageFileCrawler implements RecursiveFileWatcher.Listener {
    private static final Logger log = LoggerFactory.getLogger(PageFileCrawler.class);

    private final Path root;
    private final PageProvider pageProvider;
    private final PageTransformer pageTransformer;
    private final ExecutorService executor;
    private RecursiveFileWatcher watcher;

    @Inject
    public PageFileCrawler(
            DndConfiguration configuration,
            PageProvider pageProvider,
            PageTransformer pageTransformer) {
        this.root = Paths.get(configuration.getRoot()).toAbsolutePath();
        this.pageProvider = pageProvider;
        this.pageTransformer = pageTransformer;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void start() {
        watcher = new RecursiveFileWatcher(root);
        watcher.addListener(this);
        executor.submit(watcher);
    }

    public void stop() {
        watcher.stop();
    }

    @Override
    public void onFileAdded(Path path) {
        compilePage(path);
    }

    @Override
    public void onFileModified(Path path) {
        compilePage(path);
    }

    private void compilePage(Path path) {
        try {
            String source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            Target target = Target.forPath(root.relativize(path));
            if (path.getFileName().toString().equals("index")) {
                target = Target.forPath(root.relativize(path.getParent()));
            }
            List<WikiLink> linkList = new ArrayList<>();
            WikiPage page = pageTransformer.transformPage(source, target, linkList);
            pageProvider.addOrUpdatePage(page, linkList);
        } catch (IOException | ParsingException e) {
            log.warn(String.format("Error compiling page %s", path), e);
        }
    }

    @Override
    public void onFileRemoved(Path path) {
        Path relativePath = root.relativize(path.toAbsolutePath());
        pageProvider.deletePage(Target.forPath(relativePath));
    }
}
