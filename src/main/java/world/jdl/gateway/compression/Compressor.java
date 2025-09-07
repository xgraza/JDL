package world.jdl.gateway.compression;

/**
 * @author xgraza
 * @since 9/7/25
 */
public interface Compressor
{
    String chunk(final byte[] bytes);
}
