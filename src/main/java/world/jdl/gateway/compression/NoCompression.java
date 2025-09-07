package world.jdl.gateway.compression;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class NoCompression implements Compressor
{
    @Override
    public String chunk(byte[] bytes)
    {
        return new String(bytes);
    }
}
