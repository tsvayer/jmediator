package jmediator;

public interface MessageRegistry extends MessageResolver {
    MessageRegistry register(Class<? extends Message<?>> clazz);
}
