package world.jdl.util;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class Validator
{
    public static void checkNonNull(final Object o, final String name)
    {
        if (o != null)
        {
            return;
        }
        throw new RuntimeException("'" + name + "' cannot be null");
    }

    public static void checkInRange(final Number n, final Number min, final Number max, final String name)
    {
        if (n.doubleValue() < min.doubleValue())
        {
            throw new RuntimeException("'" + name + "' cannot be less than " + min);
        }
        if (n.doubleValue() > max.doubleValue())
        {
            throw new RuntimeException("'" + name + "' cannot be greater than " + max);
        }
    }
}
