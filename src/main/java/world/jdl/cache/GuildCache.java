package world.jdl.cache;

import world.jdl.rest.Endpoints;
import world.jdl.rest.RESTClient;
import world.jdl.structure.guild.Guild;

import java.io.IOException;
import java.util.Objects;

/**
 * @author xgraza
 * @since 9/9/25
 */
public final class GuildCache extends Cache<Long, Guild>
{
    public GuildCache(final RESTClient restClient)
    {
        super(restClient);
    }

    @Override
    public Guild fetch(final Long key)
    {
        Objects.requireNonNull(key, "key must not be null");
        try
        {
            return cache(key, restClient.getAsync(Endpoints.Guilds.GET_GUILD.modify(key)));
        } catch (IOException | InterruptedException e)
        {
            return null;
        }
    }
}
