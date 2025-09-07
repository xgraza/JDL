package world.jdl;

import world.jdl.gateway.Connection;
import world.jdl.gateway.GatewayIntent;
import world.jdl.gateway.compression.Compression;
import world.jdl.gateway.packet.client.IdentifyGatewayPacket;
import world.jdl.rest.RESTClient;
import world.jdl.util.Validator;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class JDL
{
    private final RESTClient restClient;
    private final Connection connection;

    public JDL(final String token, final int intents, final Compression compression)
            throws InterruptedException
    {
        restClient = new RESTClient(token);
        connection = new Connection(compression, token, intents);

        connection.connectBlocking();
    }

    public void login()
    {
        connection.login();
    }

    public void login(final IdentifyGatewayPacket.ConnectionProperties properties)
    {
        connection.login(properties);
    }

    public static final class Builder
    {
        private String token;
        private int intents;
        private Compression compression = Compression.NONE;

        public Builder setToken(String token)
        {
            this.token = token;
            return this;
        }

        public Builder setIntents(int intents)
        {
            this.intents = intents;
            return this;
        }

        public Builder setIntents(final GatewayIntent... intents)
        {
            int bit = 1;
            for (final GatewayIntent intent : intents)
            {
                bit |= intent.getBit();
            }
            this.intents = bit;
            return this;
        }

        public Builder setCompression(Compression compression)
        {
            this.compression = compression;
            return this;
        }

        public JDL build() throws InterruptedException
        {
            Validator.checkNonNull(token, "token");
            Validator.checkNonNull(compression, "compression");
            Validator.checkInRange(intents, 0, Integer.MAX_VALUE, "intents");
            return new JDL(token, intents, compression);
        }
    }
}
