package world.jdl.rest;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class Endpoints
{
    /**
     * Endpoints relating to a guild channel
     * @see <a href="https://discord.com/developers/docs/resources/channel#channels-resource">Discord Channels Resource</a>
     */
    public interface Channels
    {
        Endpoint<Object> GET_CHANNEL = Endpoint.of(
                Endpoint.Method.GET, "/channels/%s", Object.class);
        Endpoint<Object> CREATE_MESSAGE = Endpoint.of(
                Endpoint.Method.POST, "/channels/%s/messages", Object.class);
    }

    /**
     * Represents an endpoint
     * @param method the request method (GET, POST, PUT, PATCH, DELETE, OPTIONS, etc)
     * @param route the route excluding the base API URL
     * @param responseType the expected class response type
     * @param <T> the expected response type
     */
    public record Endpoint<T>(Method method, String route, Class<T> responseType)
    {
        public Endpoint<T> modify(final Object... params)
        {
            return new Endpoint<T>(method, String.format(route, params), responseType);
        }

        Endpoint<T> derive(final Method method)
        {
            return new Endpoint<>(method, route, responseType);
        }

        static <K> Endpoint<K> of(Method method, String route, Class<K> responseType)
        {
            return new Endpoint<>(method, route, responseType);
        }

        public enum Method
        {
            GET, POST, PATCH, PUT, DELETE, OPTIONS
        }
    }
}
