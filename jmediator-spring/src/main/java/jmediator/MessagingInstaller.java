package jmediator;

import jmediator.logging.LoggingMiddleware;
import jmediator.metrics.TimerMetricsMiddleware;
import jmediator.validation.HibernateValidationMiddleware;
import jmediator.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import java.util.logging.Logger;

@Configuration
public class MessagingInstaller {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperFactory().getObjectMapper();
    }

    @Bean
    public MessageBus messageBus(MessageDispatcher dispatcher) {
        return new DefaultMessageBus(dispatcher);
    }

    @Bean
    public MessageDispatcher messageDispatcher(MessagingMiddleware middleware) {
        return new MiddlewareMessageDispatcher(middleware);
    }

    @Bean
    public MessageRegistry messageRegistry() {
        return new AnnotationMessageRegistry();
    }

    @Bean
    public MessageHandlerRegistry messageHandlerRegistry(Environment env, MessageRegistry messageRegistry, ObjectMapper objectMapper) {
        var messageHandlerRegistry = new DefaultMessageHandlerRegistry(messageRegistry);
        return messageHandlerRegistry;
        //NOTE: Redis discovery
//        String serviceAddress = env.getProperty("jmediator.service.address");
//        return new RedisRegistrationAgent(serviceAddress, jedis(), messageHandlerRegistry, objectMapper);
    }

    @Bean
    public MessagingMiddleware entryMiddleware(MessageHandlerResolver handlerResolver, ObjectMapper objectMapper) {
        MessagingMiddleware middleware = new LocalHandlerMiddleware(handlerResolver);
        middleware = new HibernateValidationMiddleware(middleware);
        middleware = new LoggingMiddleware(
                objectMapper,
                getLogger(LoggingMiddleware.class),
                middleware);
//        middleware = new ZipkinTracingMiddleware(tracer(), middleware);
        return new TimerMetricsMiddleware(
                getLogger(TimerMetricsMiddleware.class),
                middleware);
    }

    private Logger getLogger(Class<?> forClass) {
        return Logger.getLogger(forClass.getName());
    }

//    @Bean
//    public Jedis jedis() {
//        return new Jedis(); //TODO: read redis endpoint from config
//    }

    //NOTE: Zipkin related staff, if uncommenting it here configure middleware chain as well
//    @Bean
//    public Tracer tracer() {
//        return Tracing
//                .newBuilder()
//                .reporter(reporter())
//                .build()
//                .tracer();
//    }
//
//    @Bean
//    Sender sender() {
//        return OkHttpSender.create("http://localhost:9411/api/v1/spans");
//    }
//
//    @Bean
//    Reporter<Span> reporter() {
//        return AsyncReporter
//                .builder(sender())
//                .build();
//    }
}
