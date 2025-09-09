package world.jdl.gateway;

import com.google.gson.JsonElement;
import world.jdl.gateway.event.IGatewayEvent;
import world.jdl.gateway.event.events.GuildCreateGatewayEvent;
import world.jdl.gateway.event.events.ReadyGatewayEvent;
import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.bi.HeartbeatGatewayPacket;
import world.jdl.gateway.packet.server.*;
import world.jdl.listener.IEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xgraza
 * @since 9/7/25
 */
final class GatewayPacketHandler implements IServerPacketHandler
{
    private static final Map<String, Class<? extends IGatewayEvent>> GATEWAY_EVENT_HANDLERS = new HashMap<>();

    static
    {
        GATEWAY_EVENT_HANDLERS.put("READY", ReadyGatewayEvent.class);
        GATEWAY_EVENT_HANDLERS.put("GUILD_CREATE", GuildCreateGatewayEvent.class);
    }

    private final Connection connection;

    public GatewayPacketHandler(final Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void onDispatch(final DispatchGatewayPacket packet)
    {
        final Class<? extends IGatewayEvent> eventClass = GATEWAY_EVENT_HANDLERS.get(packet.getEventName());
        if (eventClass == null)
        {
            System.out.println("Don't know how to handle " + packet.getEventName());
            return;
        }

        final JsonElement data = packet.getData();

        switch (packet.getEventName())
        {
            case "GUILD_CREATE" -> dispatchEvent(new GuildCreateGatewayEvent(data));
            default ->
            {
                final IGatewayEvent event = Connection.GSON.fromJson(packet.getData(), eventClass);
                if (event == null)
                {
                    return;
                }
                dispatchEvent(event);
            }
        }
    }

    void dispatchEvent(final IGatewayEvent event)
    {
        for (final IEventListener listener : connection.getJDL().getListeners())
        {
            event.handle(listener);
        }
    }

    @Override
    public void onReconnect(ReconnectGatewayPacket packet)
    {

    }

    @Override
    public void onInvalidSession(InvalidSessionGatewayPacket packet)
    {

    }

    @Override
    public void onHello(final HelloGatewayPacket packet)
    {
        connection.getGatewayHeartbeat().startHeartbeat(packet.getHeartbeatInterval());
    }

    @Override
    public void onHeartbeat(HeartbeatGatewayPacket packet)
    {

    }

    @Override
    public void onHeartbeatAck(final HeartbeatAckGatewayPacket packet)
    {
        final long timestampMS = System.currentTimeMillis();
        final long latency = timestampMS - connection.getGatewayHeartbeat().getLastHeartbeatMS();
        connection.setLatency(latency);
    }
}
