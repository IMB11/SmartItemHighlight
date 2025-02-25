package dev.imb11.smartitemhighlight.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SIHEvent<T> {
    private final List<T> listeners = new ArrayList<>();
    public final T invoker;

    public SIHEvent(Function<List<T>, T> invokerFactory) {
        this.invoker = invokerFactory.apply(listeners);
    }

    public void register(T listener) {
        listeners.add(listener);
    }

    public enum CallbackResult {
        CONTINUE,
        SUCCESS,
        FAIL
    }
}

