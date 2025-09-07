package world.jdl.gateway.packet;

/**
 * @author xgraza
 * @since 9/7/25
 */
public interface IGatewayPacket
{
    default void handle(IServerPacketHandler handler)
    {
        throw new RuntimeException("Not handled by client");
    }

    default OP getOP()
    {
        return OP.INVALID;
    }
}
