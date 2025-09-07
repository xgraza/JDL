package world.jdl.rest.callback;

/**
 * @author xgraza
 * @since 9/7/25
 */
@FunctionalInterface
public interface RESTErrorCallback<T extends Throwable> extends RESTCallback<T>
{
}
