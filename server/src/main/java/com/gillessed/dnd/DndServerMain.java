package com.gillessed.dnd;

import com.gillessed.dnd.bundles.auth.DndAuthBundle;
import com.gillessed.dnd.bundles.auth.DndLoginAuthenticator;
import com.gillessed.dnd.bundles.auth.DndSessionAuthenticator;
import com.gillessed.dnd.bundles.guice.DndAuthModule;
import com.gillessed.dnd.bundles.guice.DndResourcesModule;
import com.gillessed.dnd.bundles.guice.DndServicesModule;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.component.LifeCycle;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

/**
 * Created by gcole on 4/16/16.
 */
public class DndServerMain extends Application<DndConfiguration> implements LifeCycle.Listener {

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
                        .addModule(new DndResourcesModule())
                        .addModule(new DndServicesModule())
                        .addModule(new DndAuthModule())
                        .setConfigClass(DndConfiguration.class)
                        .build(Stage.DEVELOPMENT);
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(new DndAuthBundle(
                guiceBundle.getInjector().getProvider(DndSessionAuthenticator.class),
                guiceBundle.getInjector().getProvider(DndLoginAuthenticator.class)
        ));
    }

    @Override
    public void run(DndConfiguration configuration, Environment environment) throws Exception {
        enableCors(environment);

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
    public void lifeCycleStarting(LifeCycle event) {

    }

    @Override
    public void lifeCycleStarted(LifeCycle event) {

    }

    @Override
    public void lifeCycleFailure(LifeCycle event, Throwable cause) {

    }

    @Override
    public void lifeCycleStopping(LifeCycle event) {
        // TODO: save catalog.
    }

    @Override
    public void lifeCycleStopped(LifeCycle event) {

    }
}
