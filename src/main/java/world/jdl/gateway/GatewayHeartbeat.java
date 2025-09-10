package world.jdl.gateway;

import world.jdl.gateway.packet.bi.HeartbeatGatewayPacket;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author xgraza
 * @since 9/7/25
 */
final class GatewayHeartbeat
{
    private static final Timer TIMER = new Timer();

    private final Connection connection;
    private long heartbeatSentAt = -1L;

    GatewayHeartbeat(final Connection connection)
    {
        this.connection = connection;
    }

    /**
     * Starts sending a heartbeat packet for a specified time provided in the HELLO event
     * @param heartbeatIntervalMS the interval in millis
     */
    void startHeartbeat(final long heartbeatIntervalMS)
    {
        // do not start two heartbeats...
        if (heartbeatSentAt != -1L)
        {
            return;
        }
        heartbeat();
        // Begin normal heartbeat
        TIMER.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                heartbeat();
            }
        }, heartbeatIntervalMS, heartbeatIntervalMS);
    }

    /**
     * Checks if the connection is open. If it is, send a HEARTBEAT packet
     */
    private void heartbeat()
    {
        if (!connection.isOpen())
        {
            TIMER.cancel();
            throw new RuntimeException("gateway not connected!");
        }
        heartbeatSentAt = System.currentTimeMillis();
        connection.sendPacket(new HeartbeatGatewayPacket(connection.getLastSequence()));
    }

    /**
     * The last timestamp of a HEARTBEAT being dispatched
     * @return the last time a heartbeat was sent
     */
    long getHeartbeatSentAt()
    {
        return heartbeatSentAt;
    }
}
