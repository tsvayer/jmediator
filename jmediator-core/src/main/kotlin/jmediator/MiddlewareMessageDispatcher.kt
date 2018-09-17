package jmediator

import reactor.core.publisher.Mono

class MiddlewareMessageDispatcher(
  private val entryMiddleware: MessagingMiddleware
) : MessageDispatcher {

  override fun <T : Message<R>, R : Any> dispatch(message: T): Mono<R> = entryMiddleware.invoke(message)
}