package jmediator

import reactor.core.publisher.Mono

class LocalHandlerMiddleware(
        private val resolver: MessageHandlerResolver
) : MessagingMiddleware {

    override fun <T : Message<R>, R : Any> invoke(message: T): Mono<R> =
            resolver.resolve(message.javaClass)
                    .orElseThrow { NoMessageHandlerFoundException() }
                    .handle(message)
}