package world.jdl.gateway;

import com.google.gson.JsonElement;
import world.jdl.gateway.observables.HeartbeatAckObservable;
import world.jdl.observe.Observer;
import world.jdl.gateway.observables.GuildCreateGatewayObservable;
import world.jdl.gateway.observables.ReadyGatewayObservable;
import world.jdl.gateway.packet.IServerPacketHandler;
import world.jdl.gateway.packet.bi.HeartbeatGatewayPacket;
import world.jdl.gateway.packet.server.*;
import world.jdl.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xgraza
 * @since 9/7/25
 */
final class GatewayPacketHandler implements IServerPacketHandler
{
    private static final Map<String, Class<? extends Observer.Observable>> GATEWAY_OBSERVABLES = new HashMap<>();

    static
    {
        GATEWAY_OBSERVABLES.put("READY", ReadyGatewayObservable.class);
        GATEWAY_OBSERVABLES.put("GUILD_CREATE", GuildCreateGatewayObservable.class);
    }

    private final Connection connection;

    GatewayPacketHandler(final Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void onDispatch(final DispatchGatewayPacket packet)
    {
        final Observer.Observable observable = createObservable(packet.getEventName(), packet.getData());
        if (observable == null)
        {
            System.out.println("Don't know how to handle " + packet.getEventName());
            return;
        }
        connection.getJDL().dispatch(observable);
    }

    Observer.Observable createObservable(final String event, final JsonElement data)
    {
        final Class<? extends Observer.Observable> observableClass = GATEWAY_OBSERVABLES.get(event);
        if (observableClass == null)
        {
            return null;
        }
        final Constructor<?> constructor = observableClass.getConstructors()[0];
        if (constructor.getParameterCount() == 0)
        {
            return Connection.GSON.fromJson(data, observableClass);
        }
        if (constructor.getParameterCount() != 1
                || !JsonElement.class.isAssignableFrom(constructor.getParameterTypes()[0]))
        {
            throw new RuntimeException("must contain one parameter of a JsonElement");
        }
        return (Observer.Observable) ReflectionUtil.createInstance(constructor, data);
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
        final long latency = timestampMS - connection.getGatewayHeartbeat().getHeartbeatSentAt();
        connection.setLatency(latency);
        connection.getJDL().dispatch(new HeartbeatAckObservable(latency));
    }
}
