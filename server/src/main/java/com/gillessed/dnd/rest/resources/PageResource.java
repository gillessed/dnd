package com.gillessed.dnd.rest.resources;


import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.request.PageRequest;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.rest.api.response.page.PageResponse;
import com.gillessed.dnd.rest.error.DndError;
import com.gillessed.dnd.rest.error.DndException;
import com.gillessed.dnd.services.PageService;
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
    public static final String PAGES_PREFIX = "pages/";

    private final PageService pageService;

    @Inject
    public PageResource(PageService pageService) {
        this.pageService = pageService;
    }

    @POST
    public PageResponse search(PageRequest pageRequest) {
        String pagePath = pageRequest.getPage();
        String processPagePath = PAGES_PREFIX + pagePath.replace("_", File.separator);
        java.nio.file.Path pathObject = Paths.get(processPagePath);
        try {
            WikiPage page = pageService.getPage(pathObject);
            List<DirectoryEntry> directoryEntries = pageService.getDirectoryContents(pathObject);
            return new PageResponse.Builder()
                    .page(page)
                    .directoryEntries(directoryEntries)
                    .build();
        } catch (NoSuchFileException e) {
            throw new DndException(DndError.Type.WIKI_PAGE_NOT_FOUND.error(), e);
        } catch (IOException | ParsingException e) {
            throw new DndException(DndError.Type.ERROR_BUILDING_PAGE.error(), e);
        }
    }
}
