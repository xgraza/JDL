package world.jdl.gateway.event;

import world.jdl.JDL;
import world.jdl.gateway.event.events.GuildCreateGatewayEvent;
import world.jdl.gateway.event.events.ReadyGatewayEvent;
import world.jdl.listener.IEventListener;
import world.jdl.structure.guild.Guild;

import java.util.List;

/**
 * @author xgraza
 * @since 9/9/25
 */
public final class InternalEventHandler implements IEventListener
{
    private final JDL jdl;

    public InternalEventHandler(final JDL jdl)
    {
        this.jdl = jdl;
    }

    @Override
    public void onReady(final ReadyGatewayEvent event)
    {
        final List<Guild> guildList = event.getGuilds();
        for (final Guild guild : guildList)
        {
            jdl.getGuilds().cache(guild.getId().getId(), guild);
        }
    }

    @Override
    public void onGuildCreate(final GuildCreateGatewayEvent event)
    {
        final Guild guild = event.getGuild();
        if (guild != null)
        {
            jdl.getGuilds().cache(guild.getId().getId(), guild);
        }
    }
}
