package jmediator;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface MessageDispatcher {
    <T extends Message<R>, R> Mono<R> dispatch(T message);
}
