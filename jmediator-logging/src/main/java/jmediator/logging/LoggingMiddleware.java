package jmediator.logging;

import jmediator.Message;
import jmediator.MessagingMiddleware;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingMiddleware implements MessagingMiddleware {
    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final MessagingMiddleware next;

    public LoggingMiddleware(
            final ObjectMapper objectMapper,
            final Logger logger,
            final MessagingMiddleware next) {
        this.objectMapper = objectMapper;
        this.logger = logger;
        this.next = next;
    }

    @Override
    public <T extends Message<R>, R> Mono<R> invoke(T message) {
        return Mono
                .just(message)
                .doOnNext(this::log)
                .flatMap(next::invoke)
                .doOnNext(this::log);
    }

    private void log(Object obj) {
        try {
            logger.log(Level.FINE, obj.getClass().getName() + ":" + objectMapper.writeValueAsString(obj));
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> "");
        }
    }
}
