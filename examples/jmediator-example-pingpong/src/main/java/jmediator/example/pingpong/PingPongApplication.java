package jmediator.example.pingpong;

import jmediator.Message;
import jmediator.MessageHandlerRegistry;
import jmediator.MessageMapping;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SpringBootApplication
@ComponentScan(basePackages = "jmediator")
public class PingPongApplication {
    @Autowired
    MessageHandlerRegistry registry;

    public static void main(String[] args) {
        SpringApplication.run(PingPongApplication.class, args);
    }

    @PostConstruct
    void routes() {
        registry.register(Ping.class, msg -> Mono.just(new Pong(msg.getId())));
    }
}

@Data
@MessageMapping("ping/send")
class Ping implements Message<Pong> {
    @NotNull
    private String id;
}

@Data
class Pong {
    private final String echoId;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
