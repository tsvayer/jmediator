package jmediator

import java.util.HashMap

class AnnotationMessageRegistry : MessageRegistry {

  private val messageMap = HashMap<String, Class<out Message<*>>>()

  override fun register(clazz: Class<out Message<*>>): MessageRegistry =
    this.apply {
      clazz
        .getAnnotation(MessageMapping::class.java)
        .value.forEach { messageMap[it] = clazz }
    }

  override fun resolve(route: String): Class<out Message<*>>? = messageMap[route]
}