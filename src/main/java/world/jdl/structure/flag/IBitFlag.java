package world.jdl.structure.flag;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author xgraza
 * @since 9/10/25
 */
public interface IBitFlag extends IFlag
{
    /**
     * Sets a flag on or off in a bit mask
     * @param mask the bit mask
     * @param flag the flag to set
     * @param value to set that bit to on or off
     * @return the new bit mask with the flag turned on
     * @param <T> the {@link IBitFlag} type
     */
    static <T extends IBitFlag> int setFlag(final int mask, final T flag, final boolean value)
    {
        final int bit = flag.getValue();
        if (value)
        {
            return mask | bit;
        } else
        {
            return mask & ~bit;
        }
    }

    /**
     * Checks if a flag is set
     * @param mask the bit mask
     * @param flag the flag to check
     * @return if the bit is not 0
     * @param <T> the {@link IBitFlag} type
     */
    static <T extends IBitFlag> boolean isSet(final int mask, final T flag)
    {
        return (mask & flag.getValue()) != 0;
    }

    /**
     * Creates a bit mask
     * @param flags vararg {@link IBitFlag}
     * @return the bit mask with all vararg options turned to 1
     */
    static int toMask(final IBitFlag... flags)
    {
        int mask = 0;
        for (final IBitFlag flag : flags)
        {
            mask = setFlag(mask, flag, true);
        }
        return mask;
    }

    /**
     * Gets a list of {@link IBitFlag} from an int bit
     * @param type the {@link IBitFlag}.class
     * @param mask the bit mask
     * @return a {@link LinkedHashSet} of {@link T}
     * @param <T> extending {@link IBitFlag}
     */
    static <T extends IBitFlag> Set<T> flagsFrom(final Class<T> type, final int mask)
    {
        final Set<T> flags = new LinkedHashSet<>();
        for (final T flag : type.getEnumConstants())
        {
            // check if this flag's bit is set
            if (isSet(mask, flag))
            {
                flags.add(flag);
            }
        }
        return flags;
    }
}
