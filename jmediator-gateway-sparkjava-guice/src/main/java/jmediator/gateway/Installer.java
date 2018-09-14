package jmediator.gateway;

import jmediator.MessageBus;
import jmediator.MessageRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

public class Installer extends AbstractModule {
    @Override
    protected void configure() {
        bind(SparkjavaGateway.class)
                .toProvider(new Provider<>() {
                    @Inject
                    ObjectMapper objectMapper;
                    @Inject
                    MessageBus messageBus;
                    @Inject
                    MessageRegistry messageRegistry;

                    @Override
                    public SparkjavaGateway get() {
                        return new SparkjavaGateway(
                                objectMapper,
                                messageBus,
                                messageRegistry);
                    }
                }).in(Singleton.class);
        bind(RouteConfiguration.class)
                .toProvider(new Provider<>() {
                    @Inject
                    SparkjavaGateway gateway;

                    @Override
                    public RouteConfiguration get() {
                        return new RouteConfiguration().configure(gateway, 8080);
                    }
                }).asEagerSingleton();
    }
}
