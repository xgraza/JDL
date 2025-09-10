package world.jdl.gateway.observables;

import world.jdl.observe.Observer;

/**
 * @author xgraza
 * @since 9/10/25
 */
public final class HeartbeatAckObservable implements Observer.Observable
{
    private final long latency;

    public HeartbeatAckObservable(final long latency)
    {
        this.latency = latency;
    }

    public long getLatency()
    {
        return latency;
    }
}
