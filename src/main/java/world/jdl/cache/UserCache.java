package world.jdl.cache;

import world.jdl.rest.Endpoints;
import world.jdl.rest.RESTClient;
import world.jdl.structure.user.User;

import java.io.IOException;
import java.util.Objects;

/**
 * @author xgraza
 * @since 9/9/25
 */
public final class UserCache extends Cache<Long, User>
{
    public UserCache(final RESTClient restClient)
    {
        super(restClient);
    }

    @Override
    public User fetch(final Long key)
    {
        Objects.requireNonNull(key, "key must not be null");
        try
        {
            return cache(key, restClient.getAsync(Endpoints.Users.GET_USER.modify(key)));
        } catch (IOException | InterruptedException e)
        {
            return null;
        }
    }
}
