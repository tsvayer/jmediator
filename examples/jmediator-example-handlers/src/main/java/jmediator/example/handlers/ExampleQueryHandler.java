package jmediator.example.handlers;

import jmediator.example.ExampleQuery;
import jmediator.example.ExampleQueryResponse;
import jmediator.MessageHandler;
import reactor.core.publisher.Mono;

public class ExampleQueryHandler implements MessageHandler<ExampleQuery, ExampleQueryResponse> {
    @Override
    public Mono<ExampleQueryResponse> handle(ExampleQuery message) {
        return Mono.just(new ExampleQueryResponse(){{
            setResult("Result for: " + message.getFilter());
        }});
    }
}
