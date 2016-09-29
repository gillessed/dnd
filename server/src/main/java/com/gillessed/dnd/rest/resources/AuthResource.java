package com.gillessed.dnd.rest.resources;


import com.gillessed.dnd.rest.api.request.LoginRequest;
import com.gillessed.dnd.rest.api.response.ImmutableLoginResponse;
import com.gillessed.dnd.rest.services.AuthService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Singleton
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/auth")
public class AuthResource {
    private final AuthService authService;

    @Inject
    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    public Response login(LoginRequest loginRequest) {
        Optional<String> token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (token.isPresent()) {
            return Response.ok(ImmutableLoginResponse.builder().token(token.get()).build()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
