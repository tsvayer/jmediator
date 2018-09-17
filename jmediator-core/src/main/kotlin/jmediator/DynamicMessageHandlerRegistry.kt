package jmediator

import net.jodah.typetools.TypeResolver
import java.util.*
import java.util.concurrent.ConcurrentHashMap

//TODO: convert this to eager discoverer that will statically register handlers in DefaultMessageHandlerRegistry
class DynamicMessageHandlerRegistry(
  private val messageHandlers: List<MessageHandler<*, *>>
) : MessageHandlerResolver {

  private val cachedHandlers = ConcurrentHashMap<Class<*>, Optional<MessageHandler<*, *>>>()

  override fun <T : Message<R>, R : Any> resolve(messageClass: Class<T>): Optional<MessageHandler<T, R>> =
    cachedHandlers
      .computeIfAbsent(messageClass) { mc ->
        messageHandlers.stream()
          .filter { doesMatch(it, mc) }
          .findFirst()
      }.map { it as MessageHandler<T, R> }

  private fun <T> doesMatch(handler: MessageHandler<*, *>, messageClazz: Class<T>): Boolean =
    TypeResolver
      .resolveRawArguments(MessageHandler::class.java, handler.javaClass)[0]
      .isAssignableFrom(messageClazz)
}