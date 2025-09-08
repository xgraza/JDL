package world.jdl.listener;

import world.jdl.gateway.event.ReadyGatewayEvent;

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
}
