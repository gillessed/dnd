package com.gillessed.dnd.services.page.impl;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.model.page.objects.WikiLink;
import com.gillessed.dnd.model.page.objects.WikiMeta;
import com.gillessed.dnd.page.PageTransformer;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.response.status.StatusResponse;
import com.gillessed.dnd.services.page.PageProvider;
import com.gillessed.dnd.util.pathtreemap.PathTreeMap;
import com.gillessed.dnd.util.pathtreemap.PathTreeMapEntry;
import com.gillessed.dnd.util.pathtreemap.impl.PathTreeHashMap;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Singleton
public class InMemoryStorePageProviderImpl implements PageProvider {
    private static final Logger log = LoggerFactory.getLogger(InMemoryStorePageProviderImpl.class);

    private final Path root;
    private final PageTransformer pageTransformer;
    private final Set<Listener> listeners;
    private final PathTreeMap<String, Target, WikiPage> pageMap;
    private final Multimap<Target, WikiLink> linkMap;
    private final ReentrantReadWriteLock lock;

    @Inject
    public InMemoryStorePageProviderImpl(
            DndConfiguration configuration,
            PageTransformer pageTransformer) {
        this.root = Paths.get(configuration.getRoot()).normalize().toAbsolutePath();
        this.pageTransformer = pageTransformer;
        this.listeners = new HashSet<>();
        this.pageMap = new PathTreeHashMap<>();
        this.linkMap = ArrayListMultimap.create();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void start() {
        log.info("Running initial page load.");
        lock.writeLock().lock();
        try {
            try {
                Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        log.info("Loading page {}", root.relativize(file.toAbsolutePath()));
                        addPage(file.toAbsolutePath());
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                log.error("Error walking tree for initial page load.", e);
                Throwables.propagate(e);
            }
            log.info("Computing link targets.");
            computeLinks();
        } finally {
            lock.writeLock().unlock();
        }
        log.info("Finished initial page load.");
    }

    private void addPage(Path path) {
        try {
            String source = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            Target target = Target.forPath(root.relativize(path));
            if (path.getFileName().toString().equals("index")) {
                target = Target.forPath(root.relativize(path.getParent()));
            }
            List<WikiLink> linkList = new ArrayList<>();
            WikiPage page = pageTransformer.transformPage(source, target, linkList);
            pageMap.put(target, page);
            linkMap.putAll(target, linkList);
        } catch (IOException | ParsingException e) {
            log.warn(String.format("Error compiling page %s", path), e);
        }
    }

    @Override
    public WikiPage getPageByTarget(Target target) {
        lock.readLock().lock();
        try {
            return pageMap.get(target);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Target> getChildrenForTarget(Target target) {
        lock.readLock().lock();
        try {
            PathTreeMapEntry<String, Target, WikiPage> entry = pageMap.getEntry(target);
            return entry.getChildren().stream().map(PathTreeMapEntry::getKey).collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<WikiPage> search(String query) {
        query = Preconditions.checkNotNull(query).toLowerCase();
        lock.readLock().lock();
        try {
            List<WikiPage> exactMatches = new ArrayList<>();
            List<WikiPage> startWithMatches = new ArrayList<>();
            List<WikiPage> partialMatches = new ArrayList<>();
            for (WikiPage page : pageMap.valueSet()) {
                String title = page.getTitle().toLowerCase();
                if (title.equals(query)) {
                    exactMatches.add(page);
                } else if (title.startsWith(query)) {
                    startWithMatches.add(page);
                } else if (title.contains(query)) {
                    partialMatches.add(page);
                }
            }

            List<WikiPage> results = new ArrayList<>();
            Comparator<WikiPage> comparator = Comparator.comparing(WikiPage::getTitle);
            exactMatches.sort(comparator);
            startWithMatches.sort(comparator);
            partialMatches.sort(comparator);
            results.addAll(exactMatches);
            results.addAll(startWithMatches);
            results.addAll(partialMatches);
            return results;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public StatusResponse getStatus() {
        lock.readLock().lock();
        try {
            AtomicInteger totalPageCount = new AtomicInteger(0);
            AtomicInteger draftPageCount = new AtomicInteger(0);
            AtomicInteger publishedPageCount = new AtomicInteger(0);
            Collection<WikiPage> pages = pageMap.valueSet();
            pages.forEach((WikiPage page) -> {
                totalPageCount.incrementAndGet();
                if (page.getMetadata().getStatus() == WikiMeta.Status.DRAFT) {
                    draftPageCount.incrementAndGet();
                } else if (page.getMetadata().getStatus() == WikiMeta.Status.PUBLISHED) {
                    publishedPageCount.incrementAndGet();
                }
            });
            List<WikiLink> brokenLinks = linkMap.values().stream()
                    .filter((WikiLink link) -> link.getTarget() == null)
                    .collect(Collectors.toList());
            return new StatusResponse.Builder()
                    .totalPageCount(totalPageCount.get())
                    .draftPageCount(draftPageCount.get())
                    .publishedPageCount(publishedPageCount.get())
                    .linkCount(linkMap.values().size())
                    .brokenLinkCount(brokenLinks.size())
                    .brokenLinks(brokenLinks)
                    .build();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addOrUpdatePage(WikiPage page, List<WikiLink> links) {
        lock.writeLock().lock();
        try {
            pageMap.put(page.getTarget(), page);
            linkMap.removeAll(page.getTarget());
            linkMap.putAll(page.getTarget(), links);
            computeLinks();
            for (Listener listener : listeners) {
                listener.pageAddedOrUpdated(page);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void deletePage(Target target) {
        lock.writeLock().lock();
        try {
            pageMap.remove(target);
            linkMap.removeAll(target);
            computeLinks();
            for (Listener listener : listeners) {
                listener.pageDeleted(target);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void computeLinks() {
        for (Target target : linkMap.keySet()) {
            for (WikiLink wikiLink : linkMap.get(target)) {
                computeLink(wikiLink);
            }
        }
    }

    private void computeLink(WikiLink wikiLink) {
        Target linkTarget = null;
        if (wikiLink.getTargetString().startsWith("#")) {
            String regex = wikiLink.getTargetString().substring(1).replace("*", ".*");
            List<Target> matchingTargets = pageMap.keySet().stream()
                    .filter((Target target) -> target.getStringRepresentation().matches(regex))
                    .collect(Collectors.toList());
            if (matchingTargets.size() == 1) {
                linkTarget = matchingTargets.get(0);
            }
        } else {
            linkTarget = new Target(wikiLink.getTargetString());
        }
        if (linkTarget != null && pageMap.get(linkTarget) != null) {
            wikiLink.setTarget(linkTarget);
            if (wikiLink.getText().equals("#")) {
                wikiLink.setDisplayText(pageMap.get(linkTarget).getTitle());
            } else {
                wikiLink.setDisplayText(wikiLink.getText());
            }
        } else {
            wikiLink.setDisplayText(wikiLink.getTargetString());
        }
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
}
