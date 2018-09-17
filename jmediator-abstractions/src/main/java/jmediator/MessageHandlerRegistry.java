package jmediator;

public interface MessageHandlerRegistry extends MessageHandlerResolver {
  <T extends Message<R>, R> MessageHandlerRegistry register(Class<T> clazz, MessageHandler<T, R> messageHandler);
}
