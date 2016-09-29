package com.gillessed.dnd.bundles.auth;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Bundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.inject.Provider;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Created by gcole on 8/16/16.
 */
public class DndAuthBundle implements Bundle {

    private final Provider<DndSessionAuthenticator> mobileSessionTokenAuthenticator;
    private final Provider<DndLoginAuthenticator> mangaReaderLoginAuthenticator;


    public DndAuthBundle(
            Provider<DndSessionAuthenticator> mobileSessionTokenAuthenticator,
            Provider<DndLoginAuthenticator> mangaReaderLoginAuthenticator) {
        this.mobileSessionTokenAuthenticator = mobileSessionTokenAuthenticator;
        this.mangaReaderLoginAuthenticator = mangaReaderLoginAuthenticator;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {}

    @Override
    public void run(Environment environment) {
        AuthFilter mobileSessionAuthFilter = new DndSessionAuthFilter.Builder<DndPrincipal>()
                .setAuthenticator(LazyAuthenticator.from(mobileSessionTokenAuthenticator))
                .buildAuthFilter();
        AuthFilter basicAuthFilter = new BasicCredentialAuthFilter.Builder<DndPrincipal>()
                .setAuthenticator(LazyAuthenticator.from(mangaReaderLoginAuthenticator))
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
}
