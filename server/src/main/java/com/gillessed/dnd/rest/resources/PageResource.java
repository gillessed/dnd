package com.gillessed.dnd.rest.resources;


import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.request.PageRequest;
import com.gillessed.dnd.rest.api.response.DndError;
import com.gillessed.dnd.rest.api.response.ImmutableDndError;
import com.gillessed.dnd.rest.api.response.ImmutablePageResponse;
import com.gillessed.dnd.rest.api.response.PageResponse;
import com.gillessed.dnd.rest.model.page.WikiPage;
import com.gillessed.dnd.rest.services.PageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/page")
public class PageResource {
    private static final Logger log = LoggerFactory.getLogger(PageResource.class);
    private static final String PAGES_PREFIX = "pages/";

    private final PageService pageService;

    @Inject
    public PageResource(PageService pageService) {
        this.pageService = pageService;
    }

    @POST
    public Response search(PageRequest pageRequest) {
        String pagePath = pageRequest.getPage();
        String processPagePath = PAGES_PREFIX + pagePath.replace("_", File.separator);
        try {
            WikiPage page = pageService.getPage(processPagePath);
            PageResponse response = ImmutablePageResponse.builder()
                    .page(page)
                    .build();
            return Response.ok(response).build();
        } catch (NoSuchFileException e) {
            log.warn("Could not find page with path {}", pagePath);
            DndError error = ImmutableDndError.builder()
                    .errorType(DndError.Type.WIKI_PAGE_NOT_FOUND)
                    .errorMessage("Could not find the page you were looking for.")
                    .build();
            return Response.ok(error).build();
        } catch (IOException | ParsingException e) {
            log.warn("Error build page: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
