package jmediator.proxy;

import jmediator.Message;
import jmediator.MessageBus;
import jmediator.MessageMappings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jodah.typetools.TypeResolver;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class HttpMessageBusProxy implements MessageBus {
    private final ObjectMapper objectMapper;
    private final String remoteBusAddress;

    public HttpMessageBusProxy(ObjectMapper objectMapper, String remoteBusAddress) {
        this.objectMapper = objectMapper;
        this.remoteBusAddress = remoteBusAddress;
    }

    @Override
    public <T extends Message<R>, R> Mono<R> send(T message) {
        var responseClass = resolveResponseClass(message);
        var route = MessageMappings.getMessageRoute(message.getClass());

        var httpClient = new DefaultAsyncHttpClient();
        return executeRequest(httpClient, message, responseClass, buildUrl(route));
    }

    private <T extends Message<R>, R> Mono<R> executeRequest(
            DefaultAsyncHttpClient httpClient,
            T message,
            Class<R> responseClass,
            String url) {
        var future = httpClient.preparePost(url)
                .setBody(serializeRequest(message))
                .execute()
                .toCompletableFuture();
        return Mono.fromFuture(future)
                .map(Response::getResponseBody)
                .map(x -> deserializerResponse(responseClass, x));
    }

    private String buildUrl(String route) {
        return remoteBusAddress + "/" + route;
    }

    private <T extends Message<R>, R> Class<R> resolveResponseClass(T message) {
        return (Class<R>) TypeResolver.resolveRawArgument(Message.class, message.getClass());
    }

    private <R> R deserializerResponse(Class<R> responseClass, String x) {
        try {
            return objectMapper.readValue(x, responseClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null; //TODO:
        }
    }

    private String serializeRequest(Object message) {
        try {
            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; //TODO
        }
    }
}
