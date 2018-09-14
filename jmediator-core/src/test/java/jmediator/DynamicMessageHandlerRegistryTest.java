package jmediator;

import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicMessageHandlerRegistryTest {
    @Test
    public void resolve() throws Exception {
        //given:
        MessageHandler<ExampleMessage, VoidResponse> mh = x -> Mono.just(new VoidResponse());
        var registry = new DynamicMessageHandlerRegistry(Arrays.asList(mh));

        //when:
        var  handler = registry.resolve(ExampleMessage.class);

        //then:
        assertThat(handler.isPresent()).isTrue();
    }
}