package dev.imb11.smartitemhighlight;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Event<T> {
    private final List<T> listeners = new ArrayList<>();
    public final T invoker;

    public Event(Function<List<T>, T> invokerFactory) {
        this.invoker = invokerFactory.apply(listeners);
    }

    public void register(T listener) {
        listeners.add(listener);
    }

    public enum CALLBACK_RESULT {
        CONTINUE,
        SUCCESS,
        FAIL
    }
}

