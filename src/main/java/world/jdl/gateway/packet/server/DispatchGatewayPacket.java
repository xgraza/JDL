package world.jdl.gateway.packet.server;

import com.google.gson.JsonElement;
import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.OP;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class DispatchGatewayPacket implements IGatewayPacket
{
    private String eventName;
    private JsonElement data;

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

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setData(JsonElement data)
    {
        this.data = data;
    }

    public JsonElement getData()
    {
        return data;
    }
}
