package jmediator.caching;

import jmediator.Message;
import jmediator.MessagingMiddleware;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CaffeineCachingMiddleware implements MessagingMiddleware {
    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final MessagingMiddleware next;
    private final Cache<String, Object> cache = Caffeine
            .newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    public CaffeineCachingMiddleware(
            final ObjectMapper objectMapper,
            final Logger logger,
            final MessagingMiddleware next) {
        this.objectMapper = objectMapper;
        this.logger = logger;
        this.next = next;
    }

    @Override
    public <T extends Message<R>, R> Mono<R> invoke(T message) {
        String hash = getHash(message);
        return getFromCache(hash)
                .map(o -> Mono.just((R) o).doOnNext(x -> log(message)))
                .orElseGet(() -> next.invoke(message).doOnNext(x -> cache.put(hash, x)));
    }

    private Optional<Object> getFromCache(String hash) {
        return Optional.ofNullable(cache.getIfPresent(hash));
    }

    private void log(Object message) {
        logger.fine(message.getClass().getName() + ": Cache hit");
    }

    private String getHash(Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(json.getBytes());
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (JsonProcessingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
