package world.jdl.gateway.packet.client;

import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.OP;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class RequestGuildMembersGatewayPacket implements IGatewayPacket
{
    @Override
    public OP getOP()
    {
        return OP.REQUEST_GUILD_MEMBERS;
    }
}
