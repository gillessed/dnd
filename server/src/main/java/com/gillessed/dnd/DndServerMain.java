package com.gillessed.dnd;

import com.gillessed.dnd.bundles.auth.DndAuthBundle;
import com.gillessed.dnd.bundles.auth.DndAuthorizer;
import com.gillessed.dnd.bundles.auth.DndLoginAuthenticator;
import com.gillessed.dnd.bundles.auth.DndSessionAuthenticator;
import com.gillessed.dnd.bundles.guice.DndAuthModule;
import com.gillessed.dnd.bundles.guice.DndPageModule;
import com.gillessed.dnd.bundles.guice.DndResourcesModule;
import com.gillessed.dnd.bundles.guice.DndServicesModule;
import com.gillessed.dnd.rest.error.DndRuntimeExceptionMapper;
import com.gillessed.dnd.services.page.PageProvider;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class DndServerMain extends Application<DndConfiguration> implements LifeCycle.Listener {
    private final Logger log = LoggerFactory.getLogger(DndServerMain.class);

    private Injector guiceBundleInjector;
    private PageProvider pageProvider;

    public static void main(String[] args) throws Exception {
        new DndServerMain().run(args);
    }

    @Override
    public String getName() {
        return "dnd";
    }

    @Override
    public void initialize(Bootstrap<DndConfiguration> bootstrap) {
        GuiceBundle<DndConfiguration> guiceBundle =
                GuiceBundle.<DndConfiguration>newBuilder()
                        .addModule(new DndAuthModule())
                        .addModule(new DndServicesModule())
                        .addModule(new DndResourcesModule())
                        .addModule(new DndPageModule())
                        .setConfigClass(DndConfiguration.class)
                        .build(Stage.DEVELOPMENT);
        bootstrap.addBundle(guiceBundle);
        guiceBundleInjector = guiceBundle.getInjector();
        bootstrap.addBundle(new DndAuthBundle(
                guiceBundleInjector.getProvider(DndSessionAuthenticator.class),
                guiceBundleInjector.getProvider(DndLoginAuthenticator.class),
                guiceBundleInjector.getProvider(DndAuthorizer.class)
        ));
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(DndConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new DndRuntimeExceptionMapper());
        enableCors(environment);

        pageProvider = guiceBundleInjector.getProvider(PageProvider.class).get();
        pageProvider.start();

        environment.jersey().setUrlPattern("/api/*");
        environment.lifecycle().addLifeCycleListener(this);
    }

    private void enableCors(Environment environment) {
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin,X-Auth-Token");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    @Override
    public void lifeCycleStarting(LifeCycle event) {}

    @Override
    public void lifeCycleStarted(LifeCycle event) {}

    @Override
    public void lifeCycleFailure(LifeCycle event, Throwable cause) {}

    @Override
    public void lifeCycleStopping(LifeCycle event) {}

    @Override
    public void lifeCycleStopped(LifeCycle event) {}
}
