package world.jdl.gateway.packet;

/**
 * @author xgraza
 * @since 9/7/25
 */
public enum OP
{
    DISPATCH(0, Direction.RECEIVE),
    HEARTBEAT(1, Direction.BOTH),
    IDENTIFY(2, Direction.SEND),
    PRESENCE_UPDATE(3, Direction.SEND),
    VOICE_STATE_UPDATE(4, Direction.SEND),
    RESUME(6, Direction.SEND),
    RECONNECT(7, Direction.RECEIVE),
    REQUEST_GUILD_MEMBERS(8, Direction.SEND),
    INVALID_SESSION(9, Direction.RECEIVE),
    HELLO(10, Direction.RECEIVE),
    HEARTBEAT_ACK(11, Direction.RECEIVE),
    REQUEST_SOUNDBOARD_SOUNDS(31, Direction.SEND),

    INVALID(-1, Direction.NONE)
    ;

    private final int code, direction;

    OP(int code, int direction)
    {
        this.code = code;
        this.direction = direction;
    }

    public int getCode()
    {
        return code;
    }

    public int getDirection()
    {
        return direction;
    }

    public static OP fromCode(final int code)
    {
        for (final OP op : values())
        {
            if (op.getCode() == code)
            {
                return op;
            }
        }
        return INVALID;
    }

    public interface Direction
    {
        int
                NONE = -1,
                SEND = 0,
                RECEIVE = 1,
                BOTH = 2;
    }
}
