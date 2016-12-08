package com.gillessed.dnd.services.page.impl;

import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.compiler.impl.PageCompilerFactoryImpl;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.page.parser.PageParserFactory;
import com.gillessed.dnd.page.token.PageTokenizerFactory;
import com.gillessed.dnd.page.token.Token;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.services.page.PageCache;
import com.gillessed.dnd.services.page.PageCacheEntry;
import com.gillessed.dnd.services.page.PageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class PageServiceImpl implements PageService {
    private static final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

    private final PageTokenizerFactory pageTokenizerFactory;
    private final PageParserFactory pageParserFactory;
    private final PageCompilerFactoryImpl pageCompilerFactory;
    private final PageCache pageCache;
    private final PageServiceOptionalWrapper optionalWrapper;
    private final Path pagesDir;

    @Inject
    public PageServiceImpl(
            DndConfiguration root,
            PageTokenizerFactory pageTokenizerFactory,
            PageParserFactory pageParserFactory,
            PageCompilerFactoryImpl pageCompilerFactory,
            PageCache pageCache) {
        this.pageTokenizerFactory = pageTokenizerFactory;
        this.pageParserFactory = pageParserFactory;
        this.pageCompilerFactory = pageCompilerFactory;
        this.pageCache = pageCache;
        this.optionalWrapper = new PageServiceOptionalWrapperImpl(this);
        this.pagesDir = Paths.get(root.getRoot()).toAbsolutePath().normalize();
    }

    @Override
    public WikiPage getPage(Path path) throws IOException, ParsingException {
        return getPage(path, false, true);
    }

    @Override
    public WikiPage getPage(Path path, boolean hitCache, boolean parseAll) throws IOException, ParsingException {
        if (hitCache) {
            Optional<PageCacheEntry> cacheEntry = pageCache.getPageByPath(path);
            if (cacheEntry.isPresent()) {
                return cacheEntry.get().getPage();
            }
        }
        Path pathObject = path;
        if (Files.isDirectory(pathObject)) {
            pathObject = pathObject.resolve("index");
        }
        String text = new String(Files.readAllBytes(pathObject));
        List<Token> tokens = pageTokenizerFactory.createPageTokenizer().tokenize(text);
        List<Element> elements = pageParserFactory.createPageParser().parseTokens(tokens);
        WikiPage page;
        if (parseAll) {
            page = pageCompilerFactory.createPageCompiler().compilePage(elements, path);
        } else {
            page = pageCompilerFactory.createPageCompiler().compileTitle(elements, path);
        }
        pageCache.addToCache(page, path);
        return page;
    }

    @Override
    public List<WikiPage> getPagesByFilename(String filename) throws IOException {
        List<Path> matchingPaths = new ArrayList<>();
        Files.walkFileTree(pagesDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.toString().endsWith(filename)) {
                    matchingPaths.add(dir.toAbsolutePath().normalize());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(filename)) {
                    matchingPaths.add(file.toAbsolutePath().normalize());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return matchingPaths.stream()
                .filter(this::pathIsValid)
                .map((Path path) -> optional().getPage(path, true, false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectoryEntry> getDirectoryContents(Path path) throws IOException {
        Path pathObject = path.toAbsolutePath().normalize();
        if (!pathIsValid(pathObject)) {
            return null;
        }
        List<Path> contents = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path.getParent(), "*")) {
            for (Path directoryEntry : directoryStream) {
                if (Files.isDirectory(pathObject)) {
                    if (Files.isDirectory(directoryEntry) && Files.exists(directoryEntry.resolve("index"))) {
                        contents.add(directoryEntry.toAbsolutePath().normalize());
                    }
                } else {
                    if (!Files.isDirectory(directoryEntry)
                            && !directoryEntry.getFileName().toString().equals("index")) {
                        contents.add(directoryEntry);
                    }
                }
            }
        }
        return getDirectoryEntriesForPaths(contents);
    }

    private List<DirectoryEntry> getDirectoryEntriesForPaths(List<Path> paths) throws IOException {
        return paths.stream()
                .map((Path path) -> {
                    try {
                        WikiPage page = getPage(path, true, false);
                        Path relativePath = pagesDir.relativize(path.toAbsolutePath().normalize());
                        String pathString = relativePath.toString().replaceAll(File.separator, "_");
                        return new DirectoryEntry.Builder()
                                .path(pathString)
                                .title(page.getTitle())
                                .description(page.getDescription())
                                .build();
                    } catch (IOException | ParsingException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectoryEntry> getParentPaths(Path path) throws IOException {
        Path pathObject = path.toAbsolutePath().normalize();
        if (!pathIsValid(pathObject)) {
            return null;
        }
        List<DirectoryEntry> entries = new ArrayList<>();
        do {
            try {
                WikiPage page = getPage(pathObject, true, false);
                Path relativePath = pagesDir.relativize(pathObject);
                String pathString = relativePath.toString().replaceAll(File.separator, "_");
                DirectoryEntry entry = new DirectoryEntry.Builder()
                        .path(pathString)
                        .title(page.getTitle())
                        .description(page.getDescription())
                        .build();
                entries.add(entry);
            } catch (ParsingException e) {
                throw new IllegalStateException(e);
            }
            pathObject = pathObject.getParent();
        } while(!pathObject.equals(pagesDir));
        entries.remove(0);
        Collections.reverse(entries);
        return entries;
    }

    @Override
    public boolean pathIsValid(Path path) {
        if (!Files.exists(path)) {
            return false;
        }
        if (Files.isDirectory(path) && !Files.exists(path.resolve("index"))) {
            return false;
        }
        Path containingDirectory = path.getParent();
        if (!Files.exists(containingDirectory) || !Files.isDirectory(containingDirectory)) {
            return false;
        }
        return true;
    }

    @Override
    public PageServiceOptionalWrapper optional() {
        return optionalWrapper;
    }

    public class PageServiceOptionalWrapperImpl implements PageServiceOptionalWrapper {
        private final PageServiceImpl pageService;

        private PageServiceOptionalWrapperImpl(PageServiceImpl pageService) {
            this.pageService = pageService;
        }

        @Override
        public Optional<WikiPage> getPage(Path path, boolean hitCache, boolean parseAll) {
            try {
                return Optional.of(pageService.getPage(path, hitCache, parseAll));
            } catch (IOException | ParsingException e) {
                logger.warn("Could not parse {}", path, e);
                return Optional.empty();
            }
        }

        @Override
        public List<WikiPage> getPagesByFilename(String filename) {
            try {
                return pageService.getPagesByFilename(filename);
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }
    }
}
