package jmediator;

import org.junit.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultMessageDispatcherTest {
  @Test
  public void dispatch() throws Exception {
    //given:
    MessageHandler<ExampleMessage, VoidResponse> handler = msg -> Mono.just(new VoidResponse());
    var handlerRegistry = new DefaultMessageHandlerRegistry(new AnnotationMessageRegistry());
    handlerRegistry.register(ExampleMessage.class, handler);
    var middleware = new LocalHandlerMiddleware(handlerRegistry);
    var dispatcher = new MiddlewareMessageDispatcher(middleware);

    //when:
    var voidResponse = dispatcher
      .dispatch(new ExampleMessage())
      .toFuture()
      .get();

    //then:
    assertThat(voidResponse).isNotNull();
  }
}