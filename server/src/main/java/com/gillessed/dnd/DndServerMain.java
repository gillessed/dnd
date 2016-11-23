package com.gillessed.dnd;

import com.gillessed.dnd.bundles.auth.DndAuthBundle;
import com.gillessed.dnd.bundles.auth.DndLoginAuthenticator;
import com.gillessed.dnd.bundles.auth.DndSessionAuthenticator;
import com.gillessed.dnd.bundles.guice.DndAuthModule;
import com.gillessed.dnd.bundles.guice.DndPageModule;
import com.gillessed.dnd.bundles.guice.DndResourcesModule;
import com.gillessed.dnd.bundles.guice.DndServicesModule;
import com.gillessed.dnd.rest.error.DndRuntimeExceptionMapper;
import com.gillessed.dnd.services.search.index.Indexer;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

/**
 * Created by gcole on 4/16/16.
 */
public class DndServerMain extends Application<DndConfiguration> implements LifeCycle.Listener {
    private final Logger log = LoggerFactory.getLogger(DndServerMain.class);
    private final Logger indexLogger = LoggerFactory.getLogger(Indexer.class);

    private Injector guiceBundleInjector;
    private Indexer indexer;

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
                guiceBundleInjector.getProvider(DndLoginAuthenticator.class)
        ));
    }

    @Override
    public void run(DndConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new DndRuntimeExceptionMapper());
        enableCors(environment);
        //Shitty, but starts the indexer by calling the injector to actually perform injection.
        Indexer indexer = guiceBundleInjector.getProvider(Indexer.class).get();
        preIndex(Paths.get(configuration.getRoot()), indexer);
        indexLogger.info("Pre-index finished: {}", indexer.getIndex());
        indexer.start();

        environment.lifecycle().addLifeCycleListener(this);
    }

    private void preIndex(Path rootPath, Indexer indexer) throws IOException {
        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                indexLogger.info("Pre-indexing {}", file);
                indexer.indexPage(file, false);
                return FileVisitResult.CONTINUE;
            }
        });
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
        indexer.stop();
    }

    @Override
    public void lifeCycleStopped(LifeCycle event) {

    }
}
