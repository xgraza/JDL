package world.jdl.gateway;

import com.google.gson.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import world.jdl.JDL;
import world.jdl.gateway.compression.Compression;
import world.jdl.gateway.compression.Compressor;
import world.jdl.gateway.compression.NoCompression;
import world.jdl.gateway.compression.ZLibCompression;
import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.Payload;
import world.jdl.gateway.packet.OP;
import world.jdl.gateway.packet.bi.HeartbeatGatewayPacket;
import world.jdl.gateway.packet.client.*;
import world.jdl.gateway.packet.server.*;
import world.jdl.structure.Snowflake;
import world.jdl.structure.user.activity.Presence;
import world.jdl.util.ReflectionUtil;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class Connection extends WebSocketClient
{
    public static final String DISCORD_GATEWAY_WS = "wss://gateway.discord.gg/?v=10&encoding=json";
    private static final Map<OP, Class<? extends IGatewayPacket>> PACKET_OP_REFERENCE_MAP = new HashMap<>();
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setStrictness(Strictness.LENIENT)
            .registerTypeAdapter(HeartbeatGatewayPacket.class, new HeartbeatGatewayPacket.HeartbeatPacketSerializer())
            .registerTypeAdapter(HeartbeatGatewayPacket.class, new HeartbeatGatewayPacket.HeartbeatPacketDeserializer())
            .registerTypeAdapter(Snowflake.class, new Snowflake.SnowflakeSerializer())
            .registerTypeAdapter(Snowflake.class, new Snowflake.SnowflakeDeserializer())
            .create();
    private static final IdentifyGatewayPacket.ConnectionProperties DEFAULT_PROPERTIES
            = new IdentifyGatewayPacket.ConnectionProperties(
                    "linux", "JDL", "JDL");

    static
    {
        PACKET_OP_REFERENCE_MAP.put(OP.DISPATCH, DispatchGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.HEARTBEAT, HeartbeatGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.IDENTIFY, IdentifyGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.PRESENCE_UPDATE, PresenceUpdateGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.VOICE_STATE_UPDATE, VoiceStateUpdateGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.RESUME, ResumeGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.RECONNECT, ReconnectGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.REQUEST_GUILD_MEMBERS, RequestGuildMembersGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.INVALID_SESSION, InvalidSessionGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.HELLO, HelloGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.HEARTBEAT_ACK, HeartbeatAckGatewayPacket.class);
        PACKET_OP_REFERENCE_MAP.put(OP.REQUEST_SOUNDBOARD_SOUNDS, RequestSoundboardGatewayPacket.class);
    }

    private final Compressor compressor;
    private final GatewayPacketHandler packetHandler;
    private final GatewayHeartbeat gatewayHeartbeat;

    private final JDL jdl;
    private final Presence defaultPresence;
    private final String token;
    private final int intents;

    private int lastSequence;
    private long latency = 0L;

    public Connection(final JDL jdl,
                      final Presence defaultPresence,
                      final Compression compression,
                      final String token,
                      final int intents)
    {
        super(Connection.createURI(compression));
        this.jdl = jdl;
        this.defaultPresence = defaultPresence;
        this.token = token;
        this.intents = intents;

        compressor = switch (compression)
        {
            case NONE -> new NoCompression();
            case ZLIB -> new ZLibCompression();
        };
        packetHandler = new GatewayPacketHandler(this);
        gatewayHeartbeat = new GatewayHeartbeat(this);
    }

    public void login()
    {
        login(DEFAULT_PROPERTIES, defaultPresence);
    }

    public void login(final IdentifyGatewayPacket.ConnectionProperties properties)
    {
        login(properties, defaultPresence);
    }

    public void login(final Presence presence)
    {
        login(DEFAULT_PROPERTIES, presence);
    }

    public void login(final IdentifyGatewayPacket.ConnectionProperties properties,
                      final Presence presence)
    {
        sendPacket(new IdentifyGatewayPacket(token,
                properties,
                !(compressor instanceof NoCompression),
                null, // TODO
                null, // TODO
                presence,
                intents));
    }

    @Override
    public void onOpen(final ServerHandshake handshake)
    {

    }

    @Override
    public void onMessage(final ByteBuffer buffer)
    {
        final String message = compressor.chunk(buffer.array());
        if (message != null)
        {
            handleMessage(message);
        }
    }

    @Override
    public void onMessage(final String message)
    {
        handleMessage(message);
    }

    @Override
    public void onClose(final int code, final String reason, final boolean forced)
    {
        System.out.println(code + " -> " + reason + " (f=" + forced + ")");
    }

    @Override
    public void onError(final Exception e)
    {
        e.printStackTrace();
    }

    void sendPacket(final IGatewayPacket packet)
    {
        if (packet.getOP().getDirection() != OP.Direction.SEND &&
                packet.getOP().getDirection() != OP.Direction.BOTH)
        {
            throw new RuntimeException(packet.getClass().getSimpleName()
                    + " cannot be sent");
        }
        final JsonObject object = new JsonObject();
        object.addProperty("op", packet.getOP().getCode());
        object.add("d", GSON.toJsonTree(packet));
        final String stringObj = GSON.toJson(object);
        send(stringObj.getBytes(StandardCharsets.UTF_8));
    }

    private void handleMessage(final String message)
    {
        System.out.println(message);
        final Payload payload = GSON.fromJson(message, Payload.class);
        if (payload.getSequence() != null)
        {
            lastSequence = payload.getSequence();
        }
        final OP op = OP.fromCode(payload.getOp());
        if (op.equals(OP.INVALID))
        {
            return;
        }
        final Class<? extends IGatewayPacket> packetClass = PACKET_OP_REFERENCE_MAP.get(op);
        if (packetClass == null)
        {
            throw new RuntimeException("No packet association for " + op);
        }
        IGatewayPacket packet = GSON.fromJson(payload.getData(), packetClass);
        if (packet == null)
        {
            // This only happens if payload.getData() is null
            // For whatever reason, GSON does not create the type if the passed in obj is null/empty
            packet = ReflectionUtil.createInstance(packetClass);
        }
        if (packet instanceof DispatchGatewayPacket dispatchPacket)
        {
            dispatchPacket.setEventName(payload.getEventName());
            dispatchPacket.setData(payload.getData());
        }
        packet.handle(packetHandler);
    }

    GatewayHeartbeat getGatewayHeartbeat()
    {
        return gatewayHeartbeat;
    }

    public long getLatency()
    {
        return latency;
    }

    void setLatency(long latency)
    {
        this.latency = latency;
    }

    public int getLastSequence()
    {
        return lastSequence;
    }

    public JDL getJDL()
    {
        return jdl;
    }

    private static URI createURI(final Compression compression)
    {
        String url = DISCORD_GATEWAY_WS;
        if (compression != null && !compression.equals(Compression.NONE))
        {
            url += "&compress=" + compression.name().toLowerCase() + "-stream";
        }
        return URI.create(url);
    }
}
