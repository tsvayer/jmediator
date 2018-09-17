package jmediator;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MessagingMiddleware {
  <T extends Message<R>, R> Mono<R> invoke(T message);
}
