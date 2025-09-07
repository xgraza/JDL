package world.jdl.gateway.packet;

import world.jdl.gateway.packet.bi.HeartbeatGatewayPacket;
import world.jdl.gateway.packet.server.*;

/**
 * @author xgraza
 * @since 9/7/25
 */
public interface IServerPacketHandler
{
    void onDispatch(DispatchGatewayPacket packet);

    void onHeartbeat(HeartbeatGatewayPacket packet);

    void onReconnect(ReconnectGatewayPacket packet);

    void onInvalidSession(InvalidSessionGatewayPacket packet);

    void onHello(HelloGatewayPacket packet);

    void onHeartbeatAck(HeartbeatAckGatewayPacket packet);
}
