package com.gillessed.dnd.bundles.auth;


import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.UnauthorizedHandler;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.Principal;

@Priority(Priorities.AUTHENTICATION)
public class DndSessionAuthFilter<P extends Principal> extends AuthFilter<DndBearerTokenCredential, P> {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String bearerToken = requestContext.getHeaderString("X-Auth-Token");
        DndBearerTokenCredential credential = new DndBearerTokenCredentialImpl(bearerToken);
        if (!authenticate(requestContext, credential, "X-Auth-Token")) {
            throw new WebApplicationException(unauthorizedHandler.buildResponse("", "X-Auth-Token"));
        }
    }

    public static class Builder<P extends Principal> extends AuthFilterBuilder<DndBearerTokenCredential, P, DndSessionAuthFilter<P>> {

        public Builder() {
            this.setUnauthorizedHandler(new TokenUnauthorizedHandler());
            this.setRealm("X-Auth-Token");
            this.setPrefix("");
        }

        @Override
        protected DndSessionAuthFilter<P> newInstance() {
            return new DndSessionAuthFilter<>();
        }
    }

    private static class TokenUnauthorizedHandler implements UnauthorizedHandler {

        @Override
        public Response buildResponse(String prefix, String realm) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("You have an invalid authentication key")
                    .build();
        }
    }
}
