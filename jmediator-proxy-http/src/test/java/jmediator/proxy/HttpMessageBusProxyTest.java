package jmediator.proxy;

import jmediator.*;
import jmediator.example.ExampleQuery;
import jmediator.serialization.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class HttpMessageBusProxyTest {
  final static String remoteAddress = "http://localhost:8080/api";

  @Test
  public void send() throws Exception {
    var objectMapper = getObjectMapper();
    var busProxy = new HttpMessageBusProxy(objectMapper, remoteAddress);

    var query = new ExampleQuery() {{
      setFilter("from proxy");
    }};
    var response = busProxy.send(query).toFuture().get();

    System.out.println(response.getResult());
  }

  @Test
  public void sendThroughProxy() throws Exception {
    var objectMapper = getObjectMapper();
    var busProxy = new HttpMessageBusProxy(objectMapper, remoteAddress);
    var registry = new DefaultMessageHandlerRegistry(new AnnotationMessageRegistry());
    var messageBus = new DefaultMessageBus(new MiddlewareMessageDispatcher(new LocalHandlerMiddleware(registry)));
    registry.register(ExampleQuery.class, busProxy::send);

    var response = messageBus.send(new ExampleQuery() {{
      setFilter("from in memory bus through proxy");
    }}).toFuture().get();

    System.out.println(response.getResult());
  }

  private ObjectMapper getObjectMapper() {
    return new ObjectMapperFactory().getObjectMapper();
  }
}