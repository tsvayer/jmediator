package jmediator;

import com.google.inject.Inject;
import net.jodah.typetools.TypeResolver;

import java.util.Set;

public class MessageHandlersRegistration {

    @Inject
    public MessageHandlersRegistration(
            final MessageHandlerRegistry registry,
            final Set<MessageHandler> handlers) {
        handlers.forEach(x -> {
            var arguments = TypeResolver.resolveRawArguments(MessageHandler.class, x.getClass());
            Class messageClass = arguments[0];

            //NOTE: This is unsafe, but only way in Java generics? Any better?
            registry.register(messageClass, x);
        });
    }
}
