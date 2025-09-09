package world.jdl;

import world.jdl.cache.GuildCache;
import world.jdl.cache.UserCache;
import world.jdl.gateway.Connection;
import world.jdl.gateway.GatewayIntent;
import world.jdl.gateway.compression.Compression;
import world.jdl.gateway.event.InternalEventHandler;
import world.jdl.gateway.packet.client.IdentifyGatewayPacket;
import world.jdl.listener.IEventListener;
import world.jdl.rest.RESTClient;
import world.jdl.structure.user.activity.Activity;
import world.jdl.structure.user.activity.Presence;
import world.jdl.util.Validator;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class JDL
{
    private final RESTClient restClient;
    private final Connection connection;

    private final Set<IEventListener> eventListenerSet;

    private final UserCache users;
    private final GuildCache guilds;

    public JDL(final String token,
               final int intents,
               final Compression compression,
               final Set<IEventListener> eventListenerSet,
               final Presence defaultPresence)
            throws InterruptedException
    {
        this.eventListenerSet = eventListenerSet;
        eventListenerSet.add(new InternalEventHandler(this));

        restClient = new RESTClient(token);
        connection = new Connection(this, defaultPresence, compression, token, intents);
        connection.connectBlocking();

        users = new UserCache(restClient);
        guilds = new GuildCache(restClient);
    }

    public void login()
    {
        connection.login();
    }

    public void login(final IdentifyGatewayPacket.ConnectionProperties properties)
    {
        connection.login(properties);
    }

    public UserCache getUsers()
    {
        return users;
    }

    public GuildCache getGuilds()
    {
        return guilds;
    }

    public Set<IEventListener> getListeners()
    {
        return eventListenerSet;
    }

    public static final class Builder
    {
        private String token;
        private int intents;
        private Compression compression = Compression.NONE;
        private Presence presence;

        private final Set<IEventListener> eventListenerSet = new LinkedHashSet<>();

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

        public Builder setPresence(Presence presence)
        {
            this.presence = presence;
            return this;
        }

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

        public Builder addListener(final IEventListener... listeners)
        {
            Collections.addAll(eventListenerSet, listeners);
            return this;
        }

        public JDL build() throws InterruptedException
        {
            Validator.checkNonNull(token, "token");
            Validator.checkNonNull(compression, "compression");
            Validator.checkInRange(intents, 0, Integer.MAX_VALUE, "intents");
            return new JDL(token, intents, compression, eventListenerSet, presence);
        }
    }
}
