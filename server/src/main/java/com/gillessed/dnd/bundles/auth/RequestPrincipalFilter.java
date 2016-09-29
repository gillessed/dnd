package com.gillessed.dnd.bundles.auth;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by gcole on 8/16/16.
 */
public class RequestPrincipalFilter implements ContainerRequestFilter {

    public static final String PRINCIPAL_ATTRIBUTE_KEY = "principal";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = requestContext.getSecurityContext();
        if (securityContext == null) {
            return;
        }
        Principal principal = securityContext.getUserPrincipal();
        if (principal == null) {
            return;
        }
        requestContext.setProperty(PRINCIPAL_ATTRIBUTE_KEY, principal);
    }
}
