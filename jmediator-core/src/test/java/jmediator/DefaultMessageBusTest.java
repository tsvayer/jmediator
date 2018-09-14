package jmediator;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultMessageBusTest {
    @Test
    public void send() throws Exception {
        //give:
        MessageHandler<ExampleMessage, VoidResponse> handler = msg -> {
            System.out.println("handler");
            return Mono.just(new VoidResponse());
        };
        var handlerRegistry = new DefaultMessageHandlerRegistry(new AnnotationMessageRegistry());
        handlerRegistry.register(ExampleMessage.class, handler);
        var middleware = new LocalHandlerMiddleware(handlerRegistry);
        var dispatcher = new MiddlewareMessageDispatcher(middleware);
        var messageBus = new DefaultMessageBus(dispatcher);

        //when:
        var voidResponse = messageBus.send(new ExampleMessage()).toFuture().get();

        //then:
        assertThat(voidResponse).isNotNull();
    }

    @Test
    public void loggin_decorator() throws Exception{
        //given:
        MessageHandler<ExampleMessage, VoidResponse> handler = x -> Mono.just(new VoidResponse());
        var handlerRegistry = new DynamicMessageHandlerRegistry(Arrays.asList(handler));
        var middleware = new LocalHandlerMiddleware(handlerRegistry);
        var dispatcher = new MiddlewareMessageDispatcher(middleware);
        var defaultMessageBus = new DefaultMessageBus(dispatcher);
        var messageBus = new LoggingMessageBus(defaultMessageBus);

        //when:
        var response = messageBus
                .send(new ExampleMessage())
                .doOnSuccess(System.out::println)
                .toFuture()
                .get();

        //then:
        assertThat(response).isNotNull();
    }

    private class LoggingMessageBus implements MessageBus{
        private final MessageBus messageBus;

        public LoggingMessageBus(MessageBus messageBus) {
            this.messageBus = messageBus;
        }

        @Override
        public <T extends Message<R>, R> Mono<R> send(T message) {
            return Mono
                    .just(message)
                    .doOnNext(System.out::println)
                    .flatMap(messageBus::send)
                    .doOnNext(System.out::println);
        }
    }

    public static <T> AbstractObjectAssert<?, T> ensure(T actual) {
        return new ObjectAssert<>(actual);
    }
}