package com.gillessed.dnd.services.page.impl;

import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.services.page.PageProvider;
import com.gillessed.dnd.services.page.PageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PageServiceImpl implements PageService {
    private static final Logger log = LoggerFactory.getLogger(PageServiceImpl.class);

    private final PageProvider pageProvider;

    @Inject
    public PageServiceImpl(PageProvider pageProvider) {
        this.pageProvider = pageProvider;
    }

    @Override
    public WikiPage getPage(Target target) throws IOException, ParsingException {
        return pageProvider.getPageByTarget(target);
    }

    @Override
    public List<DirectoryEntry> getDirectoryContents(Target target) {
        Target parent = target.getParent();
        if (parent == null) {
            return Collections.emptyList();
        }

        return pageProvider.getChildrenForTarget(parent).stream()
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
