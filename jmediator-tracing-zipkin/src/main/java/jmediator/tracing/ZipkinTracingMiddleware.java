package jmediator.tracing;

import brave.Tracer;
import jmediator.Message;
import jmediator.MessageMappings;
import jmediator.MessagingMiddleware;
import reactor.core.publisher.Mono;

public class ZipkinTracingMiddleware implements MessagingMiddleware {
    private final MessagingMiddleware next;
    private final Tracer tracer;

    public ZipkinTracingMiddleware(Tracer tracer, MessagingMiddleware next) {
        this.next = next;
        this.tracer = tracer;
    }

    @Override
    public <T extends Message<R>, R> Mono<R> invoke(T message) {
        var span = tracer
                .nextSpan()
                .name(MessageMappings.getMessageRoute(message.getClass()))
                .start();
        var scope = tracer.withSpanInScope(span);
        return next
                .invoke(message)
                .doOnNext(x -> {
                    scope.close();
                    span.finish();
                });
    }
}
