package world.jdl.gateway.observables;

import com.google.gson.JsonElement;
import world.jdl.gateway.Connection;
import world.jdl.observe.Observer;
import world.jdl.structure.guild.Guild;

/**
 * @author xgraza
 * @since 9/9/25
 */
public final class GuildCreateGatewayObservable implements Observer.Observable
{
    private final Guild guild;

    public GuildCreateGatewayObservable(final JsonElement element)
    {
        if (!element.isJsonObject())
        {
            throw new RuntimeException("element must be a JsonObject");
        }
        guild = Connection.GSON.fromJson(element, Guild.class);
    }

    public Guild getGuild()
    {
        return guild;
    }
}
