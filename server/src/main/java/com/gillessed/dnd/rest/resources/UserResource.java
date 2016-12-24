package com.gillessed.dnd.rest.resources;

import com.gillessed.dnd.model.auth.User;
import com.gillessed.dnd.rest.api.response.user.GetRolesResponse;
import com.gillessed.dnd.rest.error.DndError;
import com.gillessed.dnd.rest.error.DndException;
import com.gillessed.dnd.services.user.UserService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Optional;

@Singleton
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/user")
public class UserResource {
    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @PermitAll
    @Path("/roles/{userId}")
    public GetRolesResponse getRolesForUser(@PathParam("userId") String userId) {
        Optional<User> userOptional = userService.getUserByUserId(userId);
        if (!userOptional.isPresent()) {
            throw new DndException(DndError.Type.NO_SUCH_USER.error());
        }
        User user = userOptional.get();
        Collection<String> roles = userService.getRolesForUser(user);
        return new GetRolesResponse.Builder()
                .roles(roles)
                .build();
    }
}
