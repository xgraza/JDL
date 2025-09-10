package world.jdl;

import world.jdl.cache.GuildCache;
import world.jdl.cache.UserCache;
import world.jdl.gateway.Connection;
import world.jdl.gateway.GatewayIntent;
import world.jdl.gateway.compression.Compression;
import world.jdl.gateway.observables.ReadyGatewayObservable;
import world.jdl.gateway.packet.client.IdentifyGatewayPacket;
import world.jdl.observe.Observer;
import world.jdl.gateway.observables.GuildCreateGatewayObservable;
import world.jdl.rest.Endpoints;
import world.jdl.rest.RESTAction;
import world.jdl.rest.RESTClient;
import world.jdl.structure.flag.IBitFlag;
import world.jdl.structure.guild.Guild;
import world.jdl.structure.user.SelfUser;
import world.jdl.structure.user.activity.Activity;
import world.jdl.structure.user.activity.Presence;
import world.jdl.util.Validator;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class Client extends Observer
{
    private final Connection connection;

    private final UserCache users;
    private final GuildCache guilds;

    /**
     * The self user, provided on the gateway READY event.
     * See {@link ReadyGatewayObservable}
     */
    private SelfUser user;

    private Client(final String token,
                   final int intents,
                   final Compression compression,
                   final Presence defaultPresence)
            throws InterruptedException
    {
        final RESTClient restClient = RESTClient.createInstance(token);
        users = new UserCache(restClient);
        guilds = new GuildCache(restClient);

        connection = new Connection(this, defaultPresence, compression, token, intents);
        connection.connectBlocking();

        on(ReadyGatewayObservable.class, this::onReady);
        on(GuildCreateGatewayObservable.class, this::onGuildCreate);
    }

    /**
     * Sends an IDENTIFY packet with default parameters
     */
    public void login()
    {
        connection.login();
    }

    /**
     * Sends an IDENTIFY packet with custom properties
     * @param properties the {@link world.jdl.gateway.packet.client.IdentifyGatewayPacket.ConnectionProperties} to customize the login
     */
    public void login(final IdentifyGatewayPacket.ConnectionProperties properties)
    {
        connection.login(properties);
    }

    private void onReady(final ReadyGatewayObservable observable)
    {
        this.user = observable.getUser();
    }

    private void onGuildCreate(final GuildCreateGatewayObservable observable)
    {
        final Guild guild = observable.getGuild();
        if (guild == null)
        {
            return;
        }
        guilds.cache(guild.getID(), guild);
    }

    /**
     * The current self user
     * @return {@link SelfUser} or null if not connected
     */
    public SelfUser getUser()
    {
        return user;
    }

    /**
     * The user cache. Auto-populated by {@link Connection} and {@link world.jdl.gateway.GatewayPacketHandler}
     * @return {@link UserCache}
     */
    public UserCache getUsers()
    {
        return users;
    }

    /**
     * The guild cache. Auto-populated by {@link Connection} and {@link world.jdl.gateway.GatewayPacketHandler}
     * @return {@link GuildCache}
     */
    public GuildCache getGuilds()
    {
        return guilds;
    }

    /**
     * Gets the time between a heartbeat sent and a heartbeat acknowledgement in milliseconds
     * @return the gateway latency
     */
    public long getGatewayLatency()
    {
        return connection.getLatency();
    }

    /**
     * Retrieves the REST ping by making a request to /gateway and recording the time in milliseconds
     * @param consumer the runnable that executes with the rest ping in milliseconds
     */
    public void getRestLatency(final Consumer<Long> consumer)
    {
        final RESTAction<Object> restAction = RESTClient.getInstance().get(Endpoints.GET_GATEWAY);
        try
        {
            final long startAt = System.currentTimeMillis();
            restAction.execute();
            final long endAt = System.currentTimeMillis();
            consumer.accept(endAt - startAt);
        } catch (IOException | InterruptedException e)
        {
            consumer.accept(-1L);
        }
    }

    /**
     * @author xgraza
     * @since 9/7/25
     */
    public static final class Builder
    {
        private String token;
        private int intents;
        private Compression compression = Compression.NONE;
        private Presence presence;

        /**
         * Sets the bot token
         * @param token the token
         * @return this {@link Builder}
         */
        public Builder setToken(String token)
        {
            this.token = token;
            return this;
        }

        /**
         * Sets the gateway intents
         * @param intents varargs {@link GatewayIntent}
         * @see <a href="https://discord.com/developers/docs/events/gateway#gateway-intents">Discord Documentation</a>
         * @return this {@link Builder}
         */
        public Builder setIntents(final GatewayIntent... intents)
        {
            this.intents = IBitFlag.toMask(intents);
            return this;
        }

        /**
         * Sets the gateway compression type
         * @param compression type of compression
         * @return this {@link Builder}
         */
        public Builder setCompression(final Compression compression)
        {
            this.compression = compression;
            return this;
        }

        /**
         * Sets the start-up {@link Presence}
         * @param presence the default presence
         * @return this {@link Builder}
         */
        public Builder setPresence(final Presence presence)
        {
            this.presence = presence;
            return this;
        }

        /**
         * Sets the default bot activities
         * @param activities varargs {@link Activity}
         * @return this {@link Builder}
         */
        public Builder setActivities(final Activity... activities)
        {
            if (presence == null)
            {
                presence = Presence.empty();
            }
            for (final Activity activity : activities)
            {
                presence.addActivity(activity);
            }
            return this;
        }

        public Client build() throws InterruptedException
        {
            Validator.checkNonNull(token, "token");
            Validator.checkNonNull(compression, "compression");
            Validator.checkInRange(intents, 0, Integer.MAX_VALUE, "intents");
            return new Client(token, intents, compression, presence);
        }
    }
}
