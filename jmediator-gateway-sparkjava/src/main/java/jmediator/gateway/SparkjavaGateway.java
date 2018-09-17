package jmediator.gateway;

import jmediator.Message;
import jmediator.MessageBus;
import jmediator.MessageRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SparkjavaGateway {
  private final MessageBus bus;
  private final MessageRegistry messageRegistry;
  private final ObjectMapper objectMapper;

  public SparkjavaGateway(
    final ObjectMapper objectMapper,
    final MessageBus bus,
    final MessageRegistry messageRegistry) {
    this.objectMapper = objectMapper;
    this.bus = bus;
    this.messageRegistry = messageRegistry;
  }

  public String handle(Request request, Response response)
    throws IOException, ExecutionException, InterruptedException {
    response.type("application/json");
    var module = request.params(":module");
    var message = request.params(":message");

    var messageClass = resolveMessageType(module, message);
    var messageInstance = deserializeMessage(request, messageClass);
    return sendMessage((Message<?>) messageInstance);
  }

  private String sendMessage(Message<?> messageInstance) throws InterruptedException, ExecutionException {
    return bus
      .send(messageInstance)
      .map(x -> ResponseEntity.ok(x, objectMapper))
      .toFuture()
      //NOTE: no async support in Spark :)))
      .get();
  }

  private Class<?> resolveMessageType(String module, String message) {
    return messageRegistry.resolve(module + "/" + message);
  }

  private Object deserializeMessage(Request request, Class<?> messageClass) throws IOException {
    Object messageInstance = null;
    if (request.requestMethod().equals("GET"))
      messageInstance = objectMapper.convertValue(getParametersMap(request), messageClass);
    else if (request.requestMethod().equals("POST"))
      messageInstance = objectMapper.readValue(request.body(), messageClass);
    return messageInstance;
  }

  private Map<String, String> getParametersMap(Request request) {
    return request
      .raw()
      .getParameterMap()
      .entrySet().stream()
      .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()[0]));
  }
}
