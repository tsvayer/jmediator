package jmediator.example.handlers

import jmediator.example.ExampleQuery
import jmediator.example.ExampleQueryResponse
import jmediator.toMono
import jmediator.MessageHandler
import reactor.core.publisher.Mono

class ExampleQueryHandler : MessageHandler<ExampleQuery, ExampleQueryResponse> {
    override fun handle(message: ExampleQuery): Mono<ExampleQueryResponse> =
            ExampleQueryResponse().apply {
                result = "(kotlin)Result for: ${message.filter}"
            }.toMono()
}
