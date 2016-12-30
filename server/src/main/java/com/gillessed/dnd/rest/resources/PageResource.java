package com.gillessed.dnd.rest.resources;


import com.gillessed.dnd.DndConfiguration;
import com.gillessed.dnd.model.auth.Roles;
import com.gillessed.dnd.model.auth.Session;
import com.gillessed.dnd.model.page.Target;
import com.gillessed.dnd.model.page.WikiPage;
import com.gillessed.dnd.page.exception.ParsingException;
import com.gillessed.dnd.rest.api.request.PageRequest;
import com.gillessed.dnd.rest.api.response.page.DirectoryEntry;
import com.gillessed.dnd.rest.api.response.page.PageResponse;
import com.gillessed.dnd.rest.error.DndError;
import com.gillessed.dnd.rest.error.DndException;
import com.gillessed.dnd.services.page.PageService;
import com.gillessed.dnd.services.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/page")
public class PageResource {
    private final PageService pageService;
    private final java.nio.file.Path pageRoot;
    private final UserService userService;
    private final Provider<Session> sessionProvider;

    @Inject
    public PageResource(
            DndConfiguration configuration,
            PageService pageService,
            UserService userService,
            Provider<Session> sessionProvider) {
        this.pageService = pageService;
        this.pageRoot = Paths.get(configuration.getRoot());
        this.userService = userService;
        this.sessionProvider = sessionProvider;
    }

    @POST
    public PageResponse getPage(PageRequest pageRequest) {
        Target target = pageRequest.getTarget();
        try {
            WikiPage page = pageService.getPage(target);
            page = removeDmContentIfNotAuthorized(page);
            List<DirectoryEntry> directoryEntries = pageService.getDirectoryContents(target);
            List<DirectoryEntry> parentEntries = pageService.getParentPaths(target);
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

    @POST
    @Path("/reload")
    public void reloadPage(PageRequest pageRequest) {
        try {
            pageService.reloadPage(pageRequest.getTarget());
        } catch (NoSuchFileException e) {
            throw new DndException(DndError.Type.WIKI_PAGE_NOT_FOUND.error(), e);
        } catch (IOException | ParsingException e) {
            throw new DndException(DndError.Type.ERROR_BUILDING_PAGE.error(), e);
        }
    }

    private WikiPage removeDmContentIfNotAuthorized(WikiPage page) {
        Session session = sessionProvider.get();
        Collection<String> roles = userService.getRolesForUser(session.getUser());
        if (!roles.contains(Roles.ADMIN) && page.getDmContent() != null) {
            return new WikiPage.Builder()
                    .from(page)
                    .dmContent(null)
                    .build();
        } else {
            return page;
        }
    }
}
