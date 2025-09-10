package world.jdl.structure.sticker;

import world.jdl.structure.flag.IFlag;

/**
 * @author xgraza
 * @since 9/10/25
 */
public enum StickerType implements IFlag
{
    STANDARD(1), GUILD(2);

    private final int value;

    StickerType(final int value)
    {
        this.value = value;
    }

    @Override
    public int getValue()
    {
        return value;
    }
}
