package world.jdl.gateway.event;

import world.jdl.listener.IEventListener;

/**
 * @author xgraza
 * @since 9/8/25
 */
public interface IGatewayEvent
{
    void handle(final IEventListener listener);
}
