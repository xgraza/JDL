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
    private long lastHeartbeatMS = -1L;

    GatewayHeartbeat(final Connection connection)
    {
        this.connection = connection;
    }

    void startHeartbeat(final long heartbeatIntervalMS)
    {
        // I know this looks stupid, but this is how they want us to do this.
        // The very first heartbeat we send should be heartbeat_interval * jitter (Math.random())
        // After that, you send a heartbeat every heartbeat_interval
        TIMER.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                heartbeat();
                System.out.println("Sent first heartbeat");

                // Begin normal heartbeat
                TIMER.scheduleAtFixedRate(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        System.out.println("Sent normal heartbeat");
                        heartbeat();
                    }
                }, heartbeatIntervalMS, heartbeatIntervalMS);
            }
        }, (long) (heartbeatIntervalMS * Math.random()));
    }

    private void heartbeat()
    {
        lastHeartbeatMS = System.currentTimeMillis();
        connection.sendPacket(new HeartbeatGatewayPacket(connection.getLastSequence()));
    }

    long getLastHeartbeatMS()
    {
        return lastHeartbeatMS;
    }
}
