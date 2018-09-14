package jmediator.gateway;

import jmediator.Message;
import jmediator.MessageBus;
import jmediator.MessageRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
public class RestControllerGateway {

    private final MessageBus bus;
    private final MessageRegistry messageRegistry;
    private final MappingJackson2HttpMessageConverter messageConverter;

    public RestControllerGateway(final MessageBus bus,
                                 final MessageRegistry messageRegistry,
                                 final MappingJackson2HttpMessageConverter messageConverter) {
        this.bus = bus;
        this.messageRegistry = messageRegistry;
        this.messageConverter = messageConverter;
    }

    @PostMapping("/api/{module}/{message}")
    public CompletableFuture<? extends ResponseEntity<?>> handlePost(
            @PathVariable("module") String module,
            @PathVariable("message") String message,
            HttpServletRequest request)
            throws IOException {
        var serverHttpRequest = new ServletServerHttpRequest(request);
        var messageClass = messageRegistry.resolve(module + "/" + message);
        var messageInstance = messageConverter.read(messageClass, serverHttpRequest);
        return send((Message<?>) messageInstance);
    }

    @GetMapping("/api/{module}/{message}")
    public CompletableFuture<? extends ResponseEntity<?>> handleGet(
            @PathVariable("module") String module,
            @PathVariable("message") String message,
            @RequestParam Map<String, Object> request) {
        var messageClass = messageRegistry.resolve(module + "/" + message);
        var messageInstance = messageConverter.getObjectMapper().convertValue(request, messageClass);
        return send(messageInstance);
    }

    private CompletableFuture<? extends ResponseEntity<?>> send(Message<?> messageInstance) {
        return bus
                .send(messageInstance)
                .map(ResponseEntity::ok)
                .toFuture();
    }
}
