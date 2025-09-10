package world.jdl.observe;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author xgraza
 * @since 9/10/25
 */
public class Observer
{
    private final Map<Class<? extends Observable>, List<Consumer<Observable>>> observableMap = new ConcurrentHashMap<>();

    /**
     * Dispatches an {@link Observable}
     * @param observable the observable object
     */
    public void dispatch(final Observable observable)
    {
        final List<Consumer<Observable>> observables = observableMap.get(observable.getClass());
        if (observables == null || observables.isEmpty())
        {
            return;
        }
        for (final Consumer<Observable> consumer : observables)
        {
            consumer.accept(observable);
        }
    }

    /**
     * Handles an observable event for an {@link Observable}
     * @param observableClass the {@link Observable}.class
     * @param handler the observable handler
     * @return the current {@link Observer}
     * @param <T> the {@link Observable} type
     */
    @SuppressWarnings("unchecked")
    public <T extends Observable> Observer on(final Class<T> observableClass, final Consumer<T> handler)
    {
        final List<Consumer<Observable>> observables = observableMap.computeIfAbsent(
                observableClass, (__) -> new LinkedList<>());
        observables.add((Consumer<Observable>) handler);
        return this;
    }

    /**
     * @author xgraza
     * @since 9/10/25
     */
    public interface Observable
    {
    }
}
