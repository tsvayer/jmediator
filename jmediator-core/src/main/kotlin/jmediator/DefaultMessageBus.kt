package jmediator

import reactor.core.publisher.Mono

class DefaultMessageBus(
        private val dispatcher: MessageDispatcher
) : MessageBus {

    override fun <T : Message<R>, R : Any> send(message: T): Mono<R> = dispatcher.dispatch(message)
}
