package jmediator;

import java.util.Optional;

@FunctionalInterface
public interface MessageHandlerResolver {
    <T extends Message<R>, R> Optional<MessageHandler<T, R>> resolve(Class<T> messageClass);
}
