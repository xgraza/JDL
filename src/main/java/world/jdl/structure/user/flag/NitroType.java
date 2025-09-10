package world.jdl.structure.user.flag;

import world.jdl.structure.flag.IFlag;

/**
 * @author xgraza
 * @since 9/10/25
 */
public enum NitroType implements IFlag
{
    NONE(0),
    CLASSIC(1),
    NITRO(2),
    BASIC(3);

    private final int value;

    NitroType(final int value)
    {
        this.value = value;
    }

    @Override
    public int getValue()
    {
        return value;
    }
}
