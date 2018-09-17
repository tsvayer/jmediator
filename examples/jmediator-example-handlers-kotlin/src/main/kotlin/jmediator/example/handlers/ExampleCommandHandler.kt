package jmediator.example.handlers

import jmediator.example.ExampleCommand
import jmediator.example.ExampleCommandResponse
import jmediator.example.ExampleQuery
import jmediator.example.domain.model.ExampleEntity
import jmediator.example.domain.model.ExampleRepository
import jmediator.MessageBus
import jmediator.MessageHandler
import reactor.core.publisher.Mono

class ExampleCommandHandler(
  private val bus: MessageBus,
  private val repository: ExampleRepository
) : MessageHandler<ExampleCommand, ExampleCommandResponse> {

  override fun handle(message: ExampleCommand): Mono<ExampleCommandResponse> {

    val entity = repository.save(ExampleEntity())

    return bus
      .send(ExampleQuery().apply { filter = "(kotlin)Some filter: ${entity.id}" })
      .map { ExampleCommandResponse().apply { echoDate = message.date } }
  }
}