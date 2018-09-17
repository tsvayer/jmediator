package jmediator;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MessageHandler<T extends Message<R>, R> {
  Mono<R> handle(T message);
}
