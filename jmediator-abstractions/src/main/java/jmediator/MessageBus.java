package jmediator;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MessageBus {
  <T extends Message<R>, R> Mono<R> send(T message);
}
