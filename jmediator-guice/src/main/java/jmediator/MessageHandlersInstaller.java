package jmediator;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.reflections.Reflections;

public class MessageHandlersInstaller extends AbstractModule {

    @Override
    protected void configure() {
        var handlerBinder = Multibinder.newSetBinder(binder(), MessageHandler.class);
        new Reflections("jmediator")
                .getSubTypesOf(MessageHandler.class)
                .forEach(x -> {
                    handlerBinder.addBinding().to(x);
                });
        bind(MessageHandlersRegistration.class).asEagerSingleton();
    }
}
