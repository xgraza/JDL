package world.jdl.gateway.event.events;

import com.google.gson.JsonElement;
import world.jdl.gateway.Connection;
import world.jdl.gateway.event.IGatewayEvent;
import world.jdl.listener.IEventListener;
import world.jdl.structure.guild.Guild;

/**
 * @author xgraza
 * @since 9/9/25
 */
public final class GuildCreateGatewayEvent implements IGatewayEvent
{
    private final Guild guild;

    public GuildCreateGatewayEvent(final JsonElement element)
    {
        if (!element.isJsonObject())
        {
            throw new RuntimeException("element must be a JsonObject");
        }
        guild = Connection.GSON.fromJson(element, Guild.class);
    }

    @Override
    public void handle(final IEventListener listener)
    {
        listener.onGuildCreate(this);
    }

    public Guild getGuild()
    {
        return guild;
    }
}
