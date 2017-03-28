package com.gillessed.dnd.services.page;

import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;

import java.io.IOException;
import java.util.List;

public interface PageService {
    WikiPage getPage(Target target) throws IOException, ParsingException;
    void reloadAll();
    void reloadPage(Target target) throws IOException, ParsingException;
    List<DirectoryEntry> getChildren(Target target);
    List<DirectoryEntry> getParentPaths(Target target);
}
