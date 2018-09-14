package jmediator;

import com.google.inject.Guice;
import org.junit.Ignore;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

public class MessagingInstallerTest {
    @org.junit.Test
    @Ignore("guice and java 10 do not work together")
    public void configure() throws Exception {
        //given:
        var injector = Guice.createInjector(
                new MessagingInstaller(),
                new MessageHandlersInstaller());
//                new MessageHandlersRegistration());

        //when:
        var bus = injector.getInstance(MessageBus.class);
        var b = bus.send(new A()).toFuture().get();

        //then:
        assertThat(bus).isNotNull();
        assertThat(b).isNotNull();
    }
}

class A implements Message<B>{}
class B {}

class AHandler implements MessageHandler<A, B>{
    @Override
    public Mono<B> handle(A message) {
        return Mono.just(new B());
    }
}
