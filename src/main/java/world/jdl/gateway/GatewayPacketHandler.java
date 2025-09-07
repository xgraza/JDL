package world.jdl.gateway;

import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.bi.HeartbeatGatewayPacket;
import world.jdl.gateway.packet.server.*;

/**
 * @author xgraza
 * @since 9/7/25
 */
final class GatewayPacketHandler implements IServerPacketHandler
{
    private final Connection connection;

    public GatewayPacketHandler(final Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void onDispatch(DispatchGatewayPacket packet)
    {

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
