package com.gillessed.dnd.services.page;

import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface PageService {

    WikiPage getPage(Path path) throws IOException, ParsingException;
    WikiPage getPage(Path path, boolean hitCache, boolean parseAll) throws IOException, ParsingException;
    List<WikiPage> getPagesByFilename(String filename) throws IOException;
    List<DirectoryEntry> getDirectoryContents(Path path) throws IOException;

    List<DirectoryEntry> getParentPaths(Path path) throws IOException;

    boolean pathIsValid(Path path);
    PageServiceOptionalWrapper optional();

    interface PageServiceOptionalWrapper {
        Optional<WikiPage> getPage(Path path, boolean hitCache, boolean parseAll);
        List<WikiPage> getPagesByFilename(String filename);
    }
}
