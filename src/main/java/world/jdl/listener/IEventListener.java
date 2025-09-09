package world.jdl.listener;

import world.jdl.gateway.event.events.GuildCreateGatewayEvent;
import world.jdl.gateway.event.events.ReadyGatewayEvent;

/**
 * @author xgraza
 * @since 9/8/25
 */
public interface IEventListener
{
    default void onReady(ReadyGatewayEvent event)
    {
        // no-op
    }

    default void onGuildCreate(GuildCreateGatewayEvent event)
    {
        // no-op
    }
}
