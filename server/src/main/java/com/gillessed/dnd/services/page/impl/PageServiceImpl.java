package com.gillessed.dnd.services.page.impl;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.page.PageTransformer;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.services.page.PageProvider;
import com.gillessed.dnd.services.page.PageService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PageServiceImpl implements PageService {
    private static final Logger log = LoggerFactory.getLogger(PageServiceImpl.class);

    private final Path rootPageDir;
    private final PageProvider pageProvider;
    private final PageTransformer pageTransformer;

    @Inject
    public PageServiceImpl(
            DndConfiguration configuration,
            PageProvider pageProvider,
            PageTransformer pageTransformer) {
        this.rootPageDir = Paths.get(configuration.getRoot()).toAbsolutePath();
        this.pageProvider = pageProvider;
        this.pageTransformer = pageTransformer;
    }

    @Override
    public WikiPage getPage(Target target) throws IOException, ParsingException {
        return pageProvider.getPageByTarget(target);
    }

    @Override
    public void reloadAll() {
        pageProvider.reloadPages();
    }

    @Override
    public void reloadPage(Target target) throws IOException, ParsingException {
        Path path = rootPageDir.resolve(target.getPath());
        if (Files.isDirectory(path)) {
            path = path.resolve("index");
        }
        String source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        List<WikiLink> linkList = new ArrayList<>();
        WikiPage page = pageTransformer.transformPage(source, target, linkList);
        pageProvider.addOrUpdatePage(page, linkList);
    }

    @Override
    public List<DirectoryEntry> getChildren(Target target) {
        return pageProvider.getChildrenForTarget(target).stream()
                .map(this::getEntryForTarget)
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectoryEntry> getParentPaths(Target target) {
        LinkedList<DirectoryEntry> directoryEntries = new LinkedList<>();
        Target parent = target.getParent();
        while (parent != null) {
            directoryEntries.addFirst(getEntryForTarget(parent));
            parent = parent.getParent();
        }
        return directoryEntries;
    }

    private DirectoryEntry getEntryForTarget(Target target) {
        WikiPage page = pageProvider.getPageByTarget(target);
        String title = target.getValue();
        String description = "";
        if (page != null) {
            title = page.getTitle();
            description = page.getMetadata().getDescription();
        }
        return new DirectoryEntry.Builder()
                .target(target)
                .title(title)
                .description(description)
                .build();
    }
}
