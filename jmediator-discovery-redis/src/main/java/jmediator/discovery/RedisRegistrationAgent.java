package jmediator.discovery;

import jmediator.proxy.HttpMessageBusProxy;
import com.fasterxml.jackson.databind.ObjectMapper;
import jmediator.Message;
import jmediator.MessageHandler;
import jmediator.MessageHandlerRegistry;
import jmediator.MessageMappings;
import redis.clients.jedis.Jedis;

import java.util.Optional;

public class RedisRegistrationAgent implements MessageHandlerRegistry {
    private final String localAddress;
    private final Jedis jedis;
    private final MessageHandlerRegistry registry;
    private final ObjectMapper objectMapper;

    public RedisRegistrationAgent(
            final String localAddress,
            final Jedis jedis,
            final MessageHandlerRegistry registry,
            final ObjectMapper objectMapper) {
        this.localAddress = localAddress;
        this.jedis = jedis;
        this.registry = registry;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T extends Message<R>, R> MessageHandlerRegistry register(Class<T> clazz, MessageHandler<T, R> messageHandler) {
        registry.register(clazz, messageHandler);
        registerToRedis(clazz);
        return this;
    }

    @Override
    public <T extends Message<R>, R> Optional<MessageHandler<T, R>> resolve(Class<T> messageClass) {
        Optional<MessageHandler<T, R>> messageHandler = registry.resolve(messageClass);
        if(messageHandler.isPresent())
            return messageHandler;

        return Optional.ofNullable(jedis.get(MessageMappings.getMessageRoute(messageClass)))
                .map(address -> new HttpMessageBusProxy(objectMapper, address)::send);
    }

    private void registerToRedis(Class<?> messageClass) {
        String route = MessageMappings.getMessageRoute(messageClass);
        jedis.set(route, localAddress);
    }
}
