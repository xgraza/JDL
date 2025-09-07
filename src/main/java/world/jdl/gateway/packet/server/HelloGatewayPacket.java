package world.jdl.gateway.packet.server;

import com.google.gson.annotations.SerializedName;
import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.OP;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class HelloGatewayPacket implements IGatewayPacket
{
    @SerializedName("heartbeat_interval")
    private int heartbeatInterval;

    @Override
    public void handle(IServerPacketHandler handler)
    {
        handler.onHello(this);
    }

    @Override
    public OP getOP()
    {
        return OP.HELLO;
    }

    public int getHeartbeatInterval()
    {
        return heartbeatInterval;
    }
}
