package world.jdl.structure.sticker;

import world.jdl.structure.flag.IFlag;

/**
 * @author xgraza
 * @since 9/10/25
 */
public enum StickerFormatType implements IFlag
{
    PNG(1),
    APNG(2),
    LOTTIE(3),
    GIF(4);

    private final int value;

    StickerFormatType(final int value)
    {
        this.value = value;
    }

    @Override
    public int getValue()
    {
        return value;
    }
}
