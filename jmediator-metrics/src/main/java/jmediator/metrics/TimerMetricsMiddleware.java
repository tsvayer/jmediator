package jmediator.metrics;

import jmediator.Message;
import jmediator.MessagingMiddleware;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.logging.Logger;

public class TimerMetricsMiddleware implements MessagingMiddleware {
    private final Logger logger;
    private final MessagingMiddleware next;

    public TimerMetricsMiddleware(
            final Logger logger,
            final MessagingMiddleware next) {
        this.logger = logger;
        this.next = next;
    }

    @Override
    public <T extends Message<R>, R> Mono<R> invoke(T message) {
        return Mono
                .just(System.nanoTime())
                .zipWith(next.invoke(message))
                .doOnNext(duration -> logDuration(message, duration))
                .map(Tuple2::getT2);
    }

    private <T extends Message<R>, R> void logDuration(T message, Tuple2<Long, R> data) {
        var start = data.getT1();
        var duration = System.nanoTime() - start;
        var logEntry = String.format("%s : %s(μ)", message.getClass().getName(), duration / 1000);
        logger.fine(logEntry);
    }
}
