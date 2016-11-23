package com.gillessed.dnd.services;

import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.compiler.PageCompilerFactory;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.page.parser.Element;
import com.gillessed.dnd.page.parser.PageParserFactory;
import com.gillessed.dnd.page.token.PageTokenizerFactory;
import com.gillessed.dnd.page.token.Token;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.rest.resources.PageResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PageService {

    private final PageTokenizerFactory pageTokenizerFactory;
    private final PageParserFactory pageParserFactory;
    private final PageCompilerFactory pageCompilerFactory;

    @Inject
    public PageService(
            PageTokenizerFactory pageTokenizerFactory,
            PageParserFactory pageParserFactory,
            PageCompilerFactory pageCompilerFactory) {
        this.pageTokenizerFactory = pageTokenizerFactory;
        this.pageParserFactory = pageParserFactory;
        this.pageCompilerFactory = pageCompilerFactory;
    }

    public WikiPage getPage(Path path) throws IOException, ParsingException {
        Path pathObject = path;
        if (Files.isDirectory(pathObject)) {
            pathObject = pathObject.resolve("index");
        }
        String text = new String(Files.readAllBytes(pathObject));
        List<Token> tokens = pageTokenizerFactory.createPageTokenizer().tokenize(text);
        List<Element> elements = pageParserFactory.createPageParser().parseTokens(tokens);
        WikiPage page = pageCompilerFactory.createPageCompiler().compilePage(elements);
        return page;
    }

    public List<DirectoryEntry> getDirectoryContents(Path path) throws IOException {
        Path pathObject = path;
        if (!Files.exists(pathObject)) {
            return null;
        }
        if (Files.isDirectory(pathObject) && !Files.exists(pathObject.resolve("index"))) {
            return null;
        }
        Path containingDirectory = pathObject.getParent();
        if (!Files.exists(containingDirectory) || !Files.isDirectory(containingDirectory)) {
            return null;
        }
        List<Path> contents = new ArrayList<>();
        DirectoryStream<Path> directorystream = Files.newDirectoryStream(containingDirectory, "*");
        for (Path directoryEntry : directorystream) {
            if (Files.isDirectory(pathObject)) {
                if (Files.isDirectory(directoryEntry) && Files.exists(directoryEntry.resolve("index"))) {
                    contents.add(directoryEntry);
                }
            } else {
                if (!Files.isDirectory(directoryEntry)
                        && !directoryEntry.getFileName().toString().equals("index")) {
                    contents.add(directoryEntry);
                }
            }
        }
        directorystream.close();
        return getDirectoryEntriesForPaths(contents);
    }

    private List<DirectoryEntry> getDirectoryEntriesForPaths(List<Path> paths) throws IOException {
        Path pagesDir = Paths.get(".", PageResource.PAGES_PREFIX).normalize();
        return paths.stream()
                .map((Path path) -> {
                    try {
                        WikiPage page = getPage(path);
                        Path relativePath = pagesDir.relativize(path);
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
}
