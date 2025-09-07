package world.jdl.rest.callback;

/**
 * @author xgraza
 * @since 9/7/25
 */
@FunctionalInterface
public interface RESTCallback<T>
{
    void callback(T value);
}
