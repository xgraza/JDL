package world.jdl.gateway.packet.bi;

import com.google.gson.*;
import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.OP;

import java.lang.reflect.Type;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class HeartbeatGatewayPacket implements IGatewayPacket
{
    private final Integer lastSequence;

    public HeartbeatGatewayPacket(final Integer lastSequence)
    {
        this.lastSequence = lastSequence;
    }

    @Override
    public void handle(IServerPacketHandler handler)
    {
        handler.onHeartbeat(this);
    }

    @Override
    public OP getOP()
    {
        return OP.HEARTBEAT;
    }

    public Integer getLastSequence()
    {
        return lastSequence;
    }

    public static final class HeartbeatPacketSerializer implements JsonSerializer<HeartbeatGatewayPacket>
    {
        @Override
        public JsonElement serialize(HeartbeatGatewayPacket packet,
                                     Type type,
                                     JsonSerializationContext ctx)
        {
            if (packet.getLastSequence() == null)
            {
                return JsonNull.INSTANCE;
            }
            return new JsonPrimitive(packet.getLastSequence());
        }
    }

    public static final class HeartbeatPacketDeserializer implements JsonDeserializer<HeartbeatGatewayPacket>
    {

        @Override
        public HeartbeatGatewayPacket deserialize(JsonElement element,
                                                  Type type,
                                                  JsonDeserializationContext ctx)
                throws JsonParseException
        {
            return new HeartbeatGatewayPacket(element.getAsJsonPrimitive().getAsInt());
        }
    }
}
