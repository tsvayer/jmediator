package jmediator;

public final class MessageMappings {

  //TODO: Should we constraint Class<?> as Class<? extends Message<?>> ?
  public static String getMessageRoute(Class<?> messageClass) {
    MessageMapping messageMap = messageClass.getDeclaredAnnotation(MessageMapping.class);
    if (messageMap != null)
      return messageMap.value()[0];

    messageClass = messageClass.getSuperclass();
    if (messageClass != null)
      return getMessageRoute(messageClass);

    return null;
  }
}
