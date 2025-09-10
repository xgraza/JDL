package world.jdl.rest;

import com.google.gson.JsonObject;
import world.jdl.structure.guild.Guild;
import world.jdl.structure.user.SelfUser;
import world.jdl.structure.user.User;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class Endpoints
{
    public static final Endpoint<Object> GET_GATEWAY = Endpoint.of(
            Endpoint.Method.GET, "/gateway", Object.class);

    public interface Users
    {
        Endpoint<SelfUser> GET_SELF = Endpoint.of(
                Endpoint.Method.GET, "/users/@me", SelfUser.class);
        Endpoint<SelfUser> MODIFY_USER = GET_SELF.derive(Endpoint.Method.PATCH);

        Endpoint<User> GET_USER = Endpoint.of(
                Endpoint.Method.GET, "/users/%s", User.class);
    }

    public interface Guilds
    {
        Endpoint<Guild> GET_GUILD = Endpoint.of(
                Endpoint.Method.GET, "/guilds/%s/?with_counts=true", Guild.class);
    }

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
