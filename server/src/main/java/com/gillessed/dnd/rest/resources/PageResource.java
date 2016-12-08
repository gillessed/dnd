package com.gillessed.dnd.rest.resources;


import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.request.PageRequest;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.rest.api.response.page.PageResponse;
import com.gillessed.dnd.rest.error.DndError;
import com.gillessed.dnd.rest.error.DndException;
import com.gillessed.dnd.services.page.PageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/page")
public class PageResource {
    private final PageService pageService;
    private final java.nio.file.Path pageRoot;

    @Inject
    public PageResource(DndConfiguration configuration, PageService pageService) {
        this.pageService = pageService;
        this.pageRoot = Paths.get(configuration.getRoot());
    }

    @POST
    public PageResponse getPage(PageRequest pageRequest) {
        String pagePath = pageRequest.getPage();
        String processPagePath = pagePath.replace("_", File.separator);
        java.nio.file.Path pathObject = pageRoot.resolve(processPagePath);
        try {
            WikiPage page = pageService.getPage(pathObject);
            List<DirectoryEntry> directoryEntries = pageService.getDirectoryContents(pathObject);
            List<DirectoryEntry> parentEntries = pageService.getParentPaths(pathObject);
            return new PageResponse.Builder()
                    .page(page)
                    .directoryEntries(directoryEntries)
                    .parentPaths(parentEntries)
                    .build();
        } catch (NoSuchFileException e) {
            throw new DndException(DndError.Type.WIKI_PAGE_NOT_FOUND.error(), e);
        } catch (IOException | ParsingException e) {
            throw new DndException(DndError.Type.ERROR_BUILDING_PAGE.error(), e);
        }
    }
}
