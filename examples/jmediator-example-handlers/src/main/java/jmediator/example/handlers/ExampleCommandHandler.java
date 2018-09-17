package jmediator.example.handlers;

import jmediator.example.ExampleCommand;
import jmediator.example.ExampleCommandResponse;
import jmediator.example.ExampleQuery;
import jmediator.example.domain.model.ExampleEntity;
import jmediator.example.domain.model.ExampleRepository;
import jmediator.MessageBus;
import jmediator.MessageHandler;
import reactor.core.publisher.Mono;

public class ExampleCommandHandler implements MessageHandler<ExampleCommand, ExampleCommandResponse> {
  private final MessageBus bus;
  private final ExampleRepository repository;

  public ExampleCommandHandler(
    final MessageBus bus,
    final ExampleRepository repository) {
    this.bus = bus;
    this.repository = repository;
  }

  @Override
  public Mono<ExampleCommandResponse> handle(ExampleCommand message) {
    var entity = repository.save(new ExampleEntity());
    return bus
      .send(new ExampleQuery() {{
        setFilter("some filter: " + entity.getId());
      }}).map(x -> new ExampleCommandResponse() {{
        setEchoDate(message.getDate());
      }});
  }
}
