package com.gillessed.dnd.rest.resources;

import com.gillessed.dnd.rest.api.response.status.StatusResponse;
import com.gillessed.dnd.services.page.PageProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/status")
public class StatusResource {

    private final PageProvider pageProvider;

    @Inject
    public StatusResource(PageProvider pageProvider) {
        this.pageProvider = pageProvider;
    }

    @GET
    @RolesAllowed("admin")
    public StatusResponse getStatus() {
        return this.pageProvider.getStatus();
    }
}
