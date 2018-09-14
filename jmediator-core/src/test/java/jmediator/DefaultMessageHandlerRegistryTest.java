package jmediator;

import org.junit.Test;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultMessageHandlerRegistryTest {
    @Test
    public void resolve() throws Exception {
        //given:
        var registry = new DefaultMessageHandlerRegistry(new AnnotationMessageRegistry());
        registry.register(ExampleMessage.class, msg -> Mono.just(new VoidResponse()));

        //when:
        var handler = registry.resolve(ExampleMessage.class);

        //then:
        assertThat(handler.isPresent()).isTrue();
    }
}

