package jmediator;

import net.jodah.typetools.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
@ComponentScan(
        basePackages = "jmediator",
        useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = MessageHandler.class)})
public class MessageHandlersInstaller {
    @Autowired
    private MessageHandlerRegistry registry;
    @Autowired(required = false)
    private List<MessageHandler> handlers = new ArrayList<>();

    @PostConstruct
    public void install() {
        handlers.forEach(x -> {
            Class[] arguments = TypeResolver.resolveRawArguments(MessageHandler.class, x.getClass());
            Class messageClass = arguments[0];

            //NOTE: This is unsafe, but only way in Java generics? Any better?
            registry.register(messageClass, x);
        });
    }
}
