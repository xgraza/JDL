package world.jdl.gateway.packet.server;

import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.OP;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class DispatchGatewayPacket implements IGatewayPacket
{
    @Override
    public void handle(IServerPacketHandler handler)
    {
        handler.onDispatch(this);
    }

    @Override
    public OP getOP()
    {
        return OP.DISPATCH;
    }
}
