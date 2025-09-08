package world.jdl.gateway.packet.client;

import world.jdl.gateway.packet.IGatewayPacket;
import world.jdl.gateway.packet.OP;
import world.jdl.structure.user.activity.Presence;

/**
 * @author xgraza
 * @since 9/7/25
 */
@SuppressWarnings("unused")
public final class IdentifyGatewayPacket implements IGatewayPacket
{
    private final String token;
    private final ConnectionProperties properties;
    private final boolean compress;
    private final Integer largeThreshold;
    private final Integer shard;
    private final Presence presence;
    private final int intents;

    public IdentifyGatewayPacket(String token,
                                 ConnectionProperties properties,
                                 boolean compress,
                                 Integer largeThreshold,
                                 Integer shard,
                                 Presence presence,
                                 int intents)
    {
        this.token = token;
        this.properties = properties;
        this.compress = compress;
        this.largeThreshold = largeThreshold;
        this.shard = shard;
        this.presence = presence;
        this.intents = intents;
    }

    @Override
    public OP getOP()
    {
        return OP.IDENTIFY;
    }

    public static final class ConnectionProperties
    {
        private final String os, browser, device;

        public ConnectionProperties(String os,
                                    String browser,
                                    String device)
        {
            this.os = os;
            this.browser = browser;
            this.device = device;
        }
    }
}
