package com.gillessed.dnd.bundles.auth;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Bundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.Authorizer;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.inject.Provider;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class DndAuthBundle implements Bundle {

    private final Provider<DndSessionAuthenticator> dndSessionTokenAuthenticator;
    private final Provider<DndLoginAuthenticator> dndLoginAuthenticator;
    private final Provider<DndAuthorizer> dndAuthorizerProvider;

    public DndAuthBundle(
            Provider<DndSessionAuthenticator> dndSessionTokenAuthenticator,
            Provider<DndLoginAuthenticator> dndLoginAuthenticator,
            Provider<DndAuthorizer> dndAuthorizerProvider) {
        this.dndSessionTokenAuthenticator = dndSessionTokenAuthenticator;
        this.dndLoginAuthenticator = dndLoginAuthenticator;
        this.dndAuthorizerProvider = dndAuthorizerProvider;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {}

    @Override
    public void run(Environment environment) {
        // Auth filter for X-Auth-Token header tokens
        AuthFilter mobileSessionAuthFilter = new DndSessionAuthFilter.Builder<DndPrincipal>()
                .setAuthenticator(LazyAuthenticator.from(dndSessionTokenAuthenticator))
                .setAuthorizer(LazyAuthorizer.from(dndAuthorizerProvider))
                .buildAuthFilter();
        // Auth filter for HTTP Basic Auth
        AuthFilter basicAuthFilter = new BasicCredentialAuthFilter.Builder<DndPrincipal>()
                .setAuthenticator(LazyAuthenticator.from(dndLoginAuthenticator))
                .setAuthorizer(LazyAuthorizer.from(dndAuthorizerProvider))
                .setRealm("Mobile")
                .buildAuthFilter();

        List<AuthFilter> filters = ImmutableList.of(mobileSessionAuthFilter, basicAuthFilter);
        environment.jersey().register(new AuthDynamicFeature(new ChainedAuthFilter(filters)));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(DndPrincipalImpl.class));
        environment.jersey().register(RequestPrincipalFilter.class);
    }

    private static final class LazyAuthenticator<C, P extends Principal> implements Authenticator<C, P> {
        private static <C, P extends Principal> Authenticator<C, P> from(Provider<? extends Authenticator<C, P>> delegateProvider) {
            return new LazyAuthenticator<>(delegateProvider);
        }

        private final Provider<? extends Authenticator<C, P>> delegate;

        private LazyAuthenticator(Provider<? extends Authenticator<C, P>> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate, "Delegate authenticator is not nullable");
        }

        @Override
        public Optional<P> authenticate(C credentials) throws AuthenticationException {
            return delegate.get().authenticate(credentials);
        }
    }

    private static final class LazyAuthorizer<P extends Principal> implements Authorizer<P> {
        private static <C, P extends Principal> Authorizer<P> from(Provider<? extends Authorizer<P>> delegateProvider) {
            return new LazyAuthorizer<>(delegateProvider);
        }

        private final Provider<? extends Authorizer<P>> delegate;

        private LazyAuthorizer(Provider<? extends Authorizer<P>> delegate) {
            this.delegate = Preconditions.checkNotNull(delegate, "Delegate authenticator is not nullable");
        }

        @Override
        public boolean authorize(P principal, String role) {
            return delegate.get().authorize(principal, role);
        }
    }
}
