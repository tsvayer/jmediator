package jmediator;

@FunctionalInterface
public interface MessageResolver {
    Class<? extends Message<?>> resolve(String route);
}
