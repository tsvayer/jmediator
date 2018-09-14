package jmediator;

import jmediator.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.*;

public class MessagingInstaller extends AbstractModule {
    @Override
    protected void configure() {
        bind(ObjectMapper.class)
                .toProvider(() -> new ObjectMapperFactory().getObjectMapper())
                .in(Singleton.class);
        bind(MessageRegistry.class)
                .to(AnnotationMessageRegistry.class)
                .in(Singleton.class);

        bind(DefaultMessageHandlerRegistry.class)
                .toProvider(new Provider<>() {
                    @Inject
                    MessageRegistry messageRegistry;

                    @Override
                    public DefaultMessageHandlerRegistry get() {
                        return new DefaultMessageHandlerRegistry(messageRegistry);
                    }
                }).in(Singleton.class);

        bind(MessageHandlerRegistry.class).to(DefaultMessageHandlerRegistry.class);
        bind(MessageHandlerResolver.class).to(DefaultMessageHandlerRegistry.class);

        bind(MessagingMiddleware.class)
                .toProvider(new Provider<>() {
                    @Inject
                    MessageHandlerResolver messageHandlerResolver;

                    @Override
                    public MessagingMiddleware get() {
                        return new LocalHandlerMiddleware(messageHandlerResolver);
                    }
                }).in(Singleton.class);

        bind(MessageDispatcher.class)
                .toProvider(new Provider<>() {
                    @Inject
                    MessagingMiddleware middleware;

                    @Override
                    public MessageDispatcher get() {
                        return new MiddlewareMessageDispatcher(middleware);
                    }
                }).in(Singleton.class);

        bind(MessageBus.class)
                .toProvider(new Provider<>() {
                    @Inject
                    MessageDispatcher messageDispatcher;

                    @Override
                    public MessageBus get() {
                        return new DefaultMessageBus(messageDispatcher);
                    }
                }).in(Singleton.class);
    }
}
