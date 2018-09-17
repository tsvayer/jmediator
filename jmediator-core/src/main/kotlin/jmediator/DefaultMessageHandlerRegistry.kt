package jmediator

import java.util.*
import java.util.concurrent.ConcurrentHashMap

class DefaultMessageHandlerRegistry(
  private val messageRegistry: MessageRegistry
) : MessageHandlerRegistry {

  private val messageHandlerMap = ConcurrentHashMap<Class<*>, MessageHandler<*, *>>()
  private val cachedHandlers = ConcurrentHashMap<Class<*>, Optional<MessageHandler<*, *>>>()

  override fun <T : Message<R>, R : Any> resolve(messageClass: Class<T>): Optional<MessageHandler<T, R>> =
    cachedHandlers
      .computeIfAbsent(messageClass) { this.findMessageHandler(it) }
      .map { it as MessageHandler<T, R> }

  override fun <T : Message<R>, R : Any> register(clazz: Class<T>, messageHandler: MessageHandler<T, R>): MessageHandlerRegistry =
    this.apply {
      messageRegistry.register(clazz)
      messageHandlerMap.put(clazz, messageHandler)
    }

  private fun findMessageHandler(messageClass: Class<*>): Optional<MessageHandler<*, *>> =
    messageHandlerMap.entries.stream()
      .filter { doesMatch(it.key, messageClass) }
      .map { it.value }
      .findFirst()

  private fun doesMatch(registeredClass: Class<*>, messageClass: Class<*>): Boolean =
    registeredClass.isAssignableFrom(messageClass)
}